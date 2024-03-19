package bg.sofia.uni.fmi.mjt.order.server.order;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

public record Order(int id, TShirt tShirt, Destination destination) {
    public static Order of(int id, String sizeStr, String colorStr, String destinationStr) {
        TShirt tShirt = TShirt.of(sizeStr, colorStr);

        Destination destination;
        try {
            destination = Destination.valueOf(destinationStr);
        } catch (IllegalArgumentException e) {
            destination = Destination.UNKNOWN;
        }

        return new Order(id, tShirt, destination);
    }
}
