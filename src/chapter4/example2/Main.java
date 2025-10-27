package chapter4.example2;

public class Main {
    static void main() throws InterruptedException {
        InventoryCounter counter = new InventoryCounter();
        DecrementInventoryThread decrementThread = new DecrementInventoryThread(counter);
        IncrementInventoryThread incrementThread = new IncrementInventoryThread(counter);

        // 이럴 경우에는 0개
        // 이유는 incrementThread가 끝낸 후 decrementThread가 시작해서
        // 10000 - 10000 = 0
//        incrementThread.start();
//        incrementThread.join();
//        decrementThread.start();
//        decrementThread.join();

        // 하지만 아래와 같이 동시에 실행되면
        // 예상과 다르게 0이 아닌 값이 나올 수 있다.
        incrementThread.start();
        decrementThread.start();
        incrementThread.join();
        decrementThread.join();
        // We currently have 468 items
        // 실행할때마다 값이 다르다.

        System.out.println("We currently have " + counter.getItems() + " items");

    }

    static class DecrementInventoryThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementInventoryThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    static class IncrementInventoryThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementInventoryThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    static class InventoryCounter {
        private int items;

        public void increment() {
            items++;
        }

        public void decrement() {
            items--;
        }

        public int getItems() {
            return items;
        }
    }
}
