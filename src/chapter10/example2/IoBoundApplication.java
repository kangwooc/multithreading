package chapter10.example2;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IoBoundApplication {
    // 스레드풀이 계속 늘어나다가 이슈가 발생
    // 운영체제에서 관리하는 스레드 수에 한계가 있기 때문
    private static final int NUM_OF_TASKS = 10000;
    static void main() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter to start");
        input.nextLine();
        System.out.printf("Running %d tasks\n", NUM_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
        long end = System.currentTimeMillis();
    }

    private static void performTasks() {
        // 10개 코어로 가상 스레드로 10000개 작업 처리
//        try (ExecutorService executor =  Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int i = 0; i < NUM_OF_TASKS; i++) {
//                executor.submit(IoBoundApplication::performIoOperation);
//            }
//        }
        // 스레드 풀의 크기를 제한하여 운영체제의 스레드 관리 한계를 피함
        // 초당 처리량을 1000개로 제한
//        try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {
//            for (int i = 0; i < NUM_OF_TASKS; i++) {
//                executor.submit(IoBoundApplication::performIoOperation);
//            }
//        }

        // 가상 스레드의 경우 스레드 생성에 제한이 없음
        // 성능이 13초로 향상됨
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < NUM_OF_TASKS; i++) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 100; j++) {
                            performIoOperation();
                        }
                    }
                });
            }
        }
    }

    private static void performIoOperation() {
        System.out.println("Performing I/O operation... " + Thread.currentThread()); // virtual thread에서는 getName() 대신 toString() 사용
        try {
            Thread.sleep(10); // Simulate a blocking I/O operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
