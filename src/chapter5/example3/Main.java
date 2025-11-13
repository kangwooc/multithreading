package chapter5.example3;

import java.util.Random;

class Main {

    static void main() {
        Intersection intersection = new Intersection();

        Thread trainAThread = new Thread(new TrainA(intersection), "Train A");
        Thread trainBThread = new Thread(new TrainB(intersection), "Train B");

        trainAThread.start();
        trainBThread.start();
    }

    static class TrainA implements Runnable {
        private Intersection intersection;
        private Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }
                intersection.takeRoadA();
            }
        }
    }

    static class TrainB implements Runnable {
        private Intersection intersection;
        private Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }
                intersection.takeRoadB();
            }
        }
    }

    static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

//        public void takeRoadA() {
//            synchronized (roadA) {
//                System.out.println("Road A is locked by " + Thread.currentThread().getName());
//                synchronized (roadB) {
//                    System.out.println("Crossing Intersection via Road A");
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }
//
//        public void takeRoadB() {
//            synchronized (roadB) {
//                System.out.println("Road B is locked by " + Thread.currentThread().getName());
//                synchronized (roadA) {
//                    System.out.println("Crossing Intersection via Road B");
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }

        // Deadlock이 발생하지 않도록 수정된 코드
        // 두 메서드 모두 동일한 순서로 락을 획득
        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by " + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Crossing Intersection via Road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road B is locked by " + Thread.currentThread().getName());
                synchronized (roadB) {
                    synchronized (roadB) {
                        System.out.println("Crossing Intersection via Road B");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }
}
