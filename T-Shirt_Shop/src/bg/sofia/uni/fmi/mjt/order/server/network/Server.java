package bg.sofia.uni.fmi.mjt.order.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int SERVER_PORT = 7777;
    private static final int MAX_EXECUTOR_THREADS = 2;

    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS)) {
            Thread.currentThread().setName("Server Thread");

            try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
                Socket client;
                while (true) {
                    client = server.accept();

                    ClientRequestHandler clientHandler = new ClientRequestHandler(client);

                    executor.execute(clientHandler);
                }
            } catch (IOException e) {
                throw new RuntimeException("There is a problem with the server socket", e);
            }
        }
    }
}
