package chapter2.example2;

import java.math.BigInteger;

public class Main {
    static void main() {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("20000"), new BigInteger("1000000")));


        thread.setDaemon(true);
        thread.start();

        thread.interrupt();
    }

    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger exponent) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(exponent) != 0; i = i.add(BigInteger.ONE)) {
                // 1. 만약 정상적으로 처리하기를 원하는 경우
//                if (Thread.currentThread().isInterrupted()) {
//                    // 인터럽트시에 계산 중단 로직
//                    System.out.println("Interrupted during long computation");
//                    return result;
//                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
