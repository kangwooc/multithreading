package chapter3.example2;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    private static final String INPUT_FILE = "src/chapter3/example2/resources/war_and_peace.txt";
    private static final int THREAD_POOL_SIZE = 10;

    static void main() throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    static void startServer(String text) throws IOException {
        // 대기열 상태는 0으로 설정
        // 이유는 접속 요청이 들어오면 스레드 풀에서 처리하게 설계
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));

        // 고정된 크기의 스레드 풀 생성
        Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        server.setExecutor(executor);
        server.start();
      }
}
