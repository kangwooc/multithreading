package chapter10.example1;

import java.util.ArrayList;
import java.util.List;

public class MultipleVirtualThreadsWithBlockingIODemo {
    private static final int NUMBER_OF_VIRTUAL_THREADS = 100;

    static void main() throws InterruptedException {
        Runnable r = () -> System.out.println("Inside thread: " + Thread.currentThread());

        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            virtualThreads.add(virtualThread);
        }

        for (Thread t : virtualThreads) {
            t.start();
        }

        for (Thread t : virtualThreads) {
            t.join();
        }
    }

    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            System.out.println("Starting blocking task in thread: " + Thread.currentThread());
            try {
                // Simulate blocking I/O operation
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Completed blocking task in thread: " + Thread.currentThread());
        }
        //Starting blocking task in thread: VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
        //Starting blocking task in thread: VirtualThread[#28]/runnable@ForkJoinPool-1-worker-2
        //Completed blocking task in thread: VirtualThread[#28]/runnable@ForkJoinPool-1-worker-2
        //Completed blocking task in thread: VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
    }
}
