package bg.sofia.uni.fmi.mjt.trading.price;

public class PriceChart implements PriceChartAPI {
    private double AMZ_Price;
    private double GOOG_Price;
    private double MSFT_Price;

    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice) {
        AMZ_Price = amazonStockPrice;
        GOOG_Price = googleStockPrice;
        MSFT_Price = microsoftStockPrice;
    }
    @Override
    public double getCurrentPrice(String stockTicker) {
        double currentPrice = switch(stockTicker) {
            case "AMZ" -> AMZ_Price;
            case "GOOG" -> GOOG_Price;
            case "MSFT" -> MSFT_Price;
            case null, default -> 0.0;
        };
        return Math.round(currentPrice * 100) / 100.0;
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if (percentChange <= 0) {
            return false;
        }
        switch (stockTicker) {
            case "AMZ":  AMZ_Price += AMZ_Price * (percentChange / 100.0); break;
            case "GOOG": GOOG_Price += GOOG_Price * (percentChange / 100.0); break;
            case "MSFT": MSFT_Price += MSFT_Price * (percentChange / 100.0); break;
            case null, default: return false;
        }
        return true;
    }
}
