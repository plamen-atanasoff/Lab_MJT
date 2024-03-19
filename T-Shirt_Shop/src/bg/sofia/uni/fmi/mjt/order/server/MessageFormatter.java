package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;

import java.util.StringJoiner;

public class MessageFormatter {
    private static final char END_OF_MESSAGE_SYMBOL = '\0';
    private static final String DISCONNECT_MESSAGE = "Disconnected from the server";
    private static final int COMMAND_POS = 0;
    private static final int SIZE_POS = 1;
    private static final int COLOR_POS = 2;
    private static final int DESTINATION_POS = 3;

    private static final int SPECIFICATION_COMMAND_POS = 1;
    private static final int ID_POS = 2;

    public static String getDisconnectMessage() {
        return DISCONNECT_MESSAGE + System.lineSeparator() + END_OF_MESSAGE_SYMBOL;
    }

    public static String getResponseText(String message, MJTOrderRepository orderRepo) {
        if (message.equals("disconnect")) {
            return getDisconnectMessage();
        }

        String[] tokens = message.split(" ");

        StringBuilder responseText = new StringBuilder();
        switch (tokens[COMMAND_POS]) {
            case "request" -> getResponseTextRequest(responseText, orderRepo,
                tokens[SIZE_POS], tokens[COLOR_POS], tokens[DESTINATION_POS]);
            case "get" -> {
                switch (tokens[SPECIFICATION_COMMAND_POS]) {
                    case "all" -> getResponseTextGetAll(responseText, orderRepo);
                    case "all-successful" -> getResponseTextGetAllSuccessful(responseText, orderRepo);
                    case "my-order" -> getResponseTextGetMyOrder(responseText, orderRepo, tokens[ID_POS]);
                }
            }
        }

        if (responseText.isEmpty()) {
            responseText.append("Unknown command");
        }

        responseText.append(System.lineSeparator());
        responseText.append('\0');

        return responseText.toString();
    }

    private static void getResponseTextRequest(
        StringBuilder responseText, MJTOrderRepository orderRepo, String... tokens) {

        String size = tokens[0].substring(tokens[0].indexOf("=") + 1);
        String color = tokens[1].substring(tokens[1].indexOf("=") + 1);
        String destination = tokens[2].substring(tokens[2].indexOf("=") + 1);

        Response response = orderRepo.request(size, color, destination);

        responseText.append("{\"status\":\"");
        responseText.append(response.status());
        responseText.append("\", \"additionalInfo\":\"");
        responseText.append(response.additionalInfo());
        responseText.append("\"}");

    }

    private static void getResponseTextGetAll(StringBuilder responseText, MJTOrderRepository orderRepo) {
        Response response = orderRepo.getAllOrders();

        responseText.append("{\"status\":\"");
        responseText.append(response.status());
        responseText.append("\", \"orders\":[");

        StringJoiner ordersString = new StringJoiner("," + System.lineSeparator());
        for (Order order : response.orders()) {
            StringBuilder orderString = getOrderString(order);
            System.out.println(orderString);
            ordersString.add(orderString);
        }

        responseText.append(ordersString);
        responseText.append("]}");
    }

    private static void getResponseTextGetAllSuccessful(StringBuilder responseText, MJTOrderRepository orderRepo) {
        Response response = orderRepo.getAllSuccessfulOrders();

        responseText.append("{\"status\":\"");
        responseText.append(response.status());
        responseText.append("\", \"orders\":[");

        StringJoiner ordersString = new StringJoiner("," + System.lineSeparator());
        for (Order order : response.orders()) {
            ordersString.add(getOrderString(order));
        }

        responseText.append(ordersString);
        responseText.append("]}");
    }

    private static void getResponseTextGetMyOrder(
        StringBuilder responseText, MJTOrderRepository orderRepo, String idString) {

        Response response = orderRepo.getOrderById(
            Integer.parseInt(idString.substring(idString.indexOf("=") + 1)));

        if (response.orders().isEmpty()) {
            responseText.append("{\"status\":\"");
            responseText.append(response.status());
            responseText.append("\", \"additionalInfo\":\"");
            responseText.append(response.additionalInfo());
            responseText.append("\"}");
        } else {
            responseText.append("{\"status\":\"");
            responseText.append(response.status());
            responseText.append("\", \"orders\":[");
            responseText.append(getOrderString(response.orders().iterator().next()));
            responseText.append("]}");
        }

    }

    private static StringBuilder getOrderString(Order order) {
        StringBuilder orderString = new StringBuilder();
        orderString.append("{\"id\":");
        orderString.append(order.id());
        orderString.append(", \"tShirt\":{\"size\":\"");
        orderString.append(order.tShirt().size().getName());
        orderString.append("\", \"color\":\"");
        orderString.append(order.tShirt().color().getName());
        orderString.append("\"}, \"destination\":\"");
        orderString.append(order.destination().getName());
        orderString.append("\"}");
        return orderString;
    }
}
