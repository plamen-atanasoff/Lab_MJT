package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.order.Order;

import java.util.ArrayList;
import java.util.Collection;

public record Response(Status status, String additionalInfo, Collection<Order> orders) {
    private enum Status {
        OK, CREATED, DECLINED, NOT_FOUND
    }

    private static final String ORDER_ID_STRING = "ORDER_ID";

    /**
     * Creates a response
     *
     * @param id order id
     * @return response with status Status.CREATED and with proper message for additional info
     */
    public static Response create(int id) {
        String additionalInfo = ORDER_ID_STRING + "=" + id;
        return new Response(Status.CREATED, additionalInfo, new ArrayList<>());
    }

    /**
     * Creates a response
     *
     * @param orders the orders which will be returned to the client
     * @return response with status Status.OK and Collection of orders
     */
    public static Response ok(Collection<Order> orders) {
        return new Response(Status.OK, "", orders);
    }

    /**
     * Creates a response
     *
     * @param errorMessage the message which will be sent as additionalInfo
     * @return response with status Status.DECLINED and errorMessage as additionalInfo
     */
    public static Response decline(String errorMessage) {
        return new Response(Status.DECLINED, errorMessage, new ArrayList<>());
    }

    /**
     * Creates a response
     *
     * @param id order id
     * @return response with status Status.NOT_FOUND and with proper message for additional info
     */
    public static Response notFound(int id) {
        String additionalInfo = "Order with id = " + id + " does not exist.";
        return new Response(Status.NOT_FOUND, additionalInfo, new ArrayList<>());
    }
}