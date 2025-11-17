package chapter10.example1;

import java.util.ArrayList;
import java.util.List;

public class MultipleVirtualThreadsDemo {
    private static final int NUMBER_OF_VIRTUAL_THREADS = 1000;
    static void main() throws InterruptedException {
        Runnable r = () -> System.out.println("Inside thread: " + Thread.currentThread());

        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(r);
            virtualThreads.add(virtualThread);
        }

        for (Thread t : virtualThreads) {
            t.start();
        }

        for (Thread t : virtualThreads) {
            t.join();
        }
    }
    //Inside thread: VirtualThread[#33]/runnable@ForkJoinPool-1-worker-8
    //Inside thread: VirtualThread[#29]/runnable@ForkJoinPool-1-worker-3
    //Inside thread: VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
    //Inside thread: VirtualThread[#34]/runnable@ForkJoinPool-1-worker-7
    //Inside thread: VirtualThread[#31]/runnable@ForkJoinPool-1-worker-5
    //Inside thread: VirtualThread[#32]/runnable@ForkJoinPool-1-worker-6
    //Inside thread: VirtualThread[#28]/runnable@ForkJoinPool-1-worker-2
    //Inside thread: VirtualThread[#36]/runnable@ForkJoinPool-1-worker-10
    //Inside thread: VirtualThread[#30]/runnable@ForkJoinPool-1-worker-4
    //Inside thread: VirtualThread[#35]/runnable@ForkJoinPool-1-worker-9
    //Inside thread: VirtualThread[#37]/runnable@ForkJoinPool-1-worker-3
    //Inside thread: VirtualThread[#40]/runnable@ForkJoinPool-1-worker-10
    //Inside thread: VirtualThread[#41]/runnable@ForkJoinPool-1-worker-4
    //Inside thread: VirtualThread[#42]/runnable@ForkJoinPool-1-worker-9
    //Inside thread: VirtualThread[#43]/runnable@ForkJoinPool-1-worker-3
    //Inside thread: VirtualThread[#44]/runnable@ForkJoinPool-1-worker-10
    //Inside thread: VirtualThread[#39]/runnable@ForkJoinPool-1-worker-2
    //Inside thread: VirtualThread[#46]/runnable@ForkJoinPool-1-worker-9
    //Inside thread: VirtualThread[#45]/runnable@ForkJoinPool-1-worker-4
    //Inside thread: VirtualThread[#38]/runnable@ForkJoinPool-1-worker-6

    // 20개인 경우 출력 예시
    // 여기서는 jvm이 가상 스레드를 10개 정도의 플랫폼 스레드에 매핑하여 실행하는 것을 볼 수 있다.
    // 제한된 플랫폼 스레드 풀 개수는 10개다. -> 이유는 10개의 CPU 코어가 있기 때문일 가능성이 높다.
}
