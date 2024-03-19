package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MJTOrderRepository implements OrderRepository {
    private enum Arguments {
        SIZE, COLOR, DESTINATION
    }

    private final AtomicInteger ordersCount;
    private final ConcurrentHashMap<Integer, Order> orders;
    private final List<Order> invalidOrders;

    public MJTOrderRepository() {
        ordersCount = new AtomicInteger(0);
        orders = new ConcurrentHashMap<>();
        invalidOrders = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public Response request(String size, String color, String destination) {
        validateArgumentNotNull(size, "size");
        validateArgumentNotNull(color, "color");
        validateArgumentNotNull(destination, "destination");

        boolean isValid = true;
        StringJoiner errorMessage = new StringJoiner(",");
        if (doesNotExistIn(size, Arguments.SIZE)) {
            System.out.println("size does not exist");
            isValid = false;
            errorMessage.add("size");
        }
        if (doesNotExistIn(color, Arguments.COLOR)) {
            System.out.println("color does not exist");
            isValid = false;
            errorMessage.add("color");
        }
        if (doesNotExistIn(destination, Arguments.DESTINATION)) {
            System.out.println("destination does not exist");
            isValid = false;
            errorMessage.add("destination");
        }

        int id = isValid ? ordersCount.getAndIncrement() : -1;

        addOrder(id, size, color, destination);

        return isValid ? Response.create(id) : Response.decline("invalid: " + errorMessage);
    }

    private void validateArgumentNotNull(String argument, String message) {
        if (argument == null) {
            throw new IllegalArgumentException(message + " is null");
        }
    }

    private boolean doesNotExistIn(String element, Arguments type) {
        return !switch (type) {
            case SIZE -> Arrays.stream(Size.values())
                .map(Size::getName)
                .anyMatch(n -> n.equals(element));
            case COLOR -> Arrays.stream(Color.values())
                .map(Color::getName)
                .anyMatch(n -> n.equals(element));
            case DESTINATION -> Arrays.stream(Destination.values())
                .map(Destination::getName)
                .anyMatch(n -> n.equals(element));
        };
    }

    private void addOrder(int id, String... args) {
        Order order = Order.of(id, args[0], args[1], args[2]);
        if (id == -1) {
            invalidOrders.add(order);
        } else {
            orders.put(id, order);
        }
    }

    @Override
    public Response getOrderById(int id) {
        if (id == -1) {
            throw new IllegalArgumentException("id is not a positive number");
        }

        Order order = orders.get(id);

        return order == null ? Response.notFound(id) : Response.ok(List.of(order));
    }

    @Override
    public Response getAllOrders() {
        Collection<Order> allOrders = new ArrayList<>(orders.values());
        allOrders.addAll(invalidOrders);

        return Response.ok(allOrders);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(orders.values());
    }
}
