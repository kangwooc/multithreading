package chapter10.example1;

public class VirtualThreadsDemo {
    static void main() throws InterruptedException {

        Runnable r = () -> System.out.println("Inside thread: " + Thread.currentThread());
        // Inside thread: Thread[#26,Thread-0,5,main]
//        Thread platformThread = new Thread(r);
//
//        platformThread.start();
//        platformThread.join();

        // 명시적으로 플랫폼 스레드를 생성 Thread[#26,Thread-0,5,main]
        Thread platformThread = Thread.ofPlatform().unstarted(r);
        platformThread.start();
        platformThread.join();

        // 가상 스레드 생성 VirtualThread[#28]/runnable@ForkJoinPool-1-worker-1
        // ForkJoinPool-1은 플랫폼 스레드로 구성된 스레드 풀
        // worker-1은 이 작업 스레드중 하나를 마운트함
        Thread virtualThread = Thread.ofVirtual().unstarted(r);
        virtualThread.start();
        virtualThread.join();
    }
}
