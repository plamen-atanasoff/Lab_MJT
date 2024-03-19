package bg.sofia.uni.fmi.mjt.order.server.tshirt;

public record TShirt(Size size, Color color) {
    public static TShirt of(String sizeString, String colorString) {
        Size size;
        try {
            size = Size.valueOf(sizeString);
        } catch (IllegalArgumentException e) {
            size = Size.UNKNOWN;
        }

        Color color;
        try {
            color = Color.valueOf(colorString);
        } catch (IllegalArgumentException e) {
            color = Color.UNKNOWN;
        }

        return new TShirt(size, color);
    }
}
