package chapter2.example1;

public class Main {
    static void main() {

        Thread thread = new Thread(new BlockingTask());

        thread.start();

        thread.interrupt();
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            // 오랜 기간 동안 sleep 상태로 가정
            // InterruptedException으로 두어짐
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread");
            }
        }
    }
}
