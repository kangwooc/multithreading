package chapter5.example1;

import java.util.Random;

public class Main {

    static void main() {
        Metrics metrics = new Metrics();

        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);

        MetricPrinter printer = new MetricPrinter(metrics);
        businessLogic1.start();
        businessLogic2.start();
        printer.start();
    }

    public static class MetricPrinter extends Thread {
        private Metrics metrics;
        public MetricPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                double average = metrics.getAverage();
                System.out.println("Average response time: " + average + " ms");
            }
        }
    }

    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();
        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }

                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            // average와 count는 여러 스레드가 동시에 접근할 수 있다.
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        // 해당 메서드는 동기화되지 않았다.
        // 따라서 average 필드가 최신 상태가 아닐 수 있다.
        public double getAverage() {
            return average;
        }
    }
}
