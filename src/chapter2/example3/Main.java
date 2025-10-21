package chapter2.example3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() {
        // Implementation goes here
        List<Long> list = List.of(0L, 3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L);
        // 팩토리얼 계산
        List<FactorialThread> factorialThreads = new ArrayList<>();

        for (long inputNumber: list) {
            FactorialThread factorialThread = new FactorialThread(inputNumber);
            factorialThreads.add(factorialThread);
        }

        for (FactorialThread factorialThread: factorialThreads) {
            factorialThread.setDaemon(true);
            factorialThread.start();
        }

        for (FactorialThread factorialThread: factorialThreads) {
            try {
                factorialThread.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < factorialThreads.size(); i++) {
            FactorialThread factorialThread = factorialThreads.get(i);
            if (factorialThread.isFinished) {
                System.out.println("Factorial of " + list.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("Factorial of " + list.get(i) + " is still in progress");
            }
        }
    }

    private static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }


        @Override
        public void run() {
            this.result = computeFactorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger computeFactorial(long number) {
            BigInteger factorial = BigInteger.ONE;
            for (long i = number; i > 0; i--) {
                factorial = factorial.multiply(BigInteger.valueOf(i));
            }
            return factorial;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
