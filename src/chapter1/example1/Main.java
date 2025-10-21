package chapter1.example1;

public class Main {
    static void main(String[] args) throws InterruptedException {
//       Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // code that will run in a new thread
//                System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
//            }
//        });

        Thread thread = new Thread(() -> {
            // code that will run in a new thread
            System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
            System.out.println("Current Thread priority: " + Thread.currentThread().getPriority());
        });

        // 이름 설정 가능
        thread.setName("New worker thread");
        // 컨텍스트 스위칭에서 동적 우선순위의 정적요소 설정
        // 우선하도록
        thread.setPriority(Thread.MAX_PRIORITY);
//        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                System.out.println("Thread " + t.getName() + " has been uncaught " + e.getMessage());
//            }
//        });

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");
        // 이 시간이 지나기 전까지 스레드 스케쥴링을 하지 말아라.
//        Thread.sleep(10000);
    }
}
