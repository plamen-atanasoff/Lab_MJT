package bg.sofia.uni.fmi.mjt.order.server.network;

import bg.sofia.uni.fmi.mjt.order.server.MessageFormatter;
import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private static MJTOrderRepository orderRepo = new MJTOrderRepository();
    private Socket socket;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            while ((message = clientReader.readLine()) != null) {

                String responseText = MessageFormatter.getResponseText(message, orderRepo);

                clientWriter.println(responseText);

                if (message.equals("disconnect")) {
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the client socket", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
