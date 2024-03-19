package bg.sofia.uni.fmi.mjt.order.server.network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", SERVER_PORT);
             InputStreamReader serverReader = new InputStreamReader(socket.getInputStream());
             PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
             Scanner consoleScanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Client thread " + socket.getLocalPort());

            while (true) {
                System.out.print("Enter message: ");
                String message = consoleScanner.nextLine();

                serverWriter.println(message);

                int c;
                while ((c = serverReader.read()) != 0 && c != -1) {
                    System.out.print((char) c);
                }
                serverReader.read();
                serverReader.read();

                if (message.equals("disconnect")) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the socket", e);
        }
    }
}
