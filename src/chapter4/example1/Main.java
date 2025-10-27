package chapter4.example1;

public class Main {
    static void main() {
        int x = 1;
        int y = 2;

        int result = sum(x, y); // cpu 레지스터에 부름
    }

    static int sum(int x, int y) {
        return x + y; // 결과가 cpu 레지스터로 들어감
    }
}