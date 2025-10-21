package chapter1.example2;

import java.util.List;
import java.util.Random;

public class Main {
    private static final int MAX_PASSWORD = 9999;
    static void main() {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

        // 다형성...
        List<Thread> threads = List.of(new AscendingHackerThread(vault), new DescendingHackerThread(vault), new PoliceThread());

        for (Thread thread : threads) {
            thread.start();
        }
    }

    private static class Vault {
        private int password;
        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int guess) throws InterruptedException {
            Thread.sleep(5000);
            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting thread " + this.getName());
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread {
        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i <= MAX_PASSWORD; i++) {
                try {
                    if (vault.isCorrectPassword(i)) {
                        System.out.println(this.getName() + " guessed the password " + i);
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Ending thread " + this.getName());
        }
    }

    private static class DescendingHackerThread extends HackerThread {
        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD; i >= 0; --i) {
                try {
                    if (vault.isCorrectPassword(i)) {
                        System.out.println(this.getName() + " guessed the password " + i);
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Ending thread " + this.getName());
        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i > 0; --i) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(i);
            }
            System.out.println("Game over for you hacker");
            System.exit(0);
        }
    }
}
