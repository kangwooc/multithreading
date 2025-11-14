package chapter6.example2;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static final int HIGHEST_PRICE = 1000;

    static void main() {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
            inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });

        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for (int i = 0; i < numberOfReaderThreads; i++) {
            Thread reader = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    int upperBound = random.nextInt(HIGHEST_PRICE);
                    int lowerBound = upperBound > 0 ? random.nextInt(upperBound) : 0;
                    inventoryDatabase.getNumberOfItemInPriceRange(lowerBound, upperBound);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }

        long startTime = System.currentTimeMillis();
        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    public static class InventoryDatabase {
        private TreeMap<Integer, Integer> inventory = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock(); // 읽기 시간이 1100ms ~ 2000ms 소요
        private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(); // 431ms ~ 585ms
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        public int getNumberOfItemInPriceRange(int lowerBound, int upperBound) {
//            lock.lock();
            readLock.lock();
            try {
                Integer fromKey = inventory.ceilingKey(lowerBound);
                Integer toKey = inventory.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> sorted = inventory.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfItems : sorted.values()) {
                    sum += numberOfItems;
                }

                return sum;
            } finally {
//                lock.unlock();
                readLock.unlock();
            }
        }

        public void addItem(int price) {
//            lock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = inventory.get(price);
                if (numberOfItemsForPrice == null) {
                    inventory.put(price, 1);
                } else {
                    inventory.put(price, numberOfItemsForPrice + 1);
                }
            } finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
//            lock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = inventory.get(price);
                if (numberOfItemsForPrice != null) {
                    if (numberOfItemsForPrice == 1) {
                        inventory.remove(price);
                    } else {
                        inventory.put(price, numberOfItemsForPrice - 1);
                    }
                }
            } finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }
    }
}
