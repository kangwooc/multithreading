package chapter8.example2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class StackTest {
    static void main() throws InterruptedException {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
//        StandardStack<Integer> stack = new StandardStack<>();
        Random random = new Random();

        int pushingThreads = 2;
        int poppingThreads = 2;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < pushingThreads; i++) {
            Thread pushThread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            pushThread.setDaemon(true);
            threads.add(pushThread);
        }


        for (int i = 0; i < poppingThreads; i++) {
            Thread popThread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            popThread.setDaemon(true);
            threads.add(popThread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(10000);

        // 락이 있는 경우 = 60549308, 65393227
        // 락이 없는 경우 = 720860388 -> 10배 이상 성능 향상이 발생함
        System.out.println("Standard Stack operation count: " + stack.getCounter());
    }

    private static class LockFreeStack<T> {
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> newHeadNode = new StackNode<>(value);

            while (true) {
                StackNode<T> currentHeadNode = head.get();
                newHeadNode.next = currentHeadNode;
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHeadNode = head.get();
            StackNode<T> newHeadNode;

            while (currentHeadNode != null) {
                newHeadNode = currentHeadNode.next;
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                   break;
                } else {
                    LockSupport.parkNanos(1);
                    currentHeadNode = head.get();
                }
            }
            counter.incrementAndGet();
            return currentHeadNode != null ? currentHeadNode.value : null;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {
            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        // 한번에 하나의 스레드만 접근 가능하도록 동기화
        // 헤드 변수에 직접적으로 쓰는 것은 Race Condition 발생 가능성 있음
        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }
            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        public synchronized int getCounter() {
            return counter;
        }
    }

    private static class StackNode<T> {
        final T value;
        StackNode<T> next;

        StackNode(T value) {
            this.value = value;
            this.next = next;
        }
    }
}
