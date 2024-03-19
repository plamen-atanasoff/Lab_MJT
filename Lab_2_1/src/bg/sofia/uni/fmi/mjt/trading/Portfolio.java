package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;

import java.time.LocalDateTime;

//make a function for rounding up to 2 decimal places
public class Portfolio implements PortfolioAPI {
    private static PriceChartAPI priceChart;
    private final String owner;
    private double budget;
    private StockPurchase[] purchases;
    private int sizeStocks = 0;

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        assert(maxSize > 0);
        Portfolio.priceChart = priceChart;
        this.owner = owner;
        this.budget = budget;
        purchases = new StockPurchase[maxSize];
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this(owner, priceChart, budget, maxSize);
        assert(stockPurchases != null);
        assert(stockPurchases.length <= maxSize);
        int counter = 0;
        for (StockPurchase stockPurchase : stockPurchases) {
            if (stockPurchase == null) {
                break;
            }
            counter++;
        }
        System.arraycopy(stockPurchases, 0, purchases, 0, counter);
        sizeStocks = counter;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (quantity <= 0) {
            return null;
        }
        if (purchases != null && purchases.length == sizeStocks) {
            return null;
        }
        double totalPrice = quantity * priceChart.getCurrentPrice(stockTicker);
        if (budget < totalPrice) {
            return null;
        }

        LocalDateTime timestamp = LocalDateTime.now();
        StockPurchase purchase = switch(stockTicker) {
            case "AMZ" ->
                    new AmazonStockPurchase(quantity, timestamp, priceChart.getCurrentPrice("AMZ"));
            case "GOOG" ->
                    new GoogleStockPurchase(quantity, timestamp, priceChart.getCurrentPrice("GOOG"));
            case "MSFT" ->
                    new MicrosoftStockPurchase(quantity, timestamp, priceChart.getCurrentPrice("MSFT"));
            case null, default -> null;
        };

        if (purchase == null) {
            return null;
        }
        priceChart.changeStockPrice(stockTicker, 5);
        budget -= totalPrice;
        purchases[sizeStocks++] = purchase;

        return purchase;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return purchases == null ? null : purchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        if (purchases == null) {
            return new StockPurchase[0];
        }
        StockPurchase[] filteredPurchases = new StockPurchase[purchases.length];
        int counter = 0;
        for (int i = 0; i < sizeStocks; ++i) {
            StockPurchase purchase = purchases[i];
            if (purchase.getPurchaseTimestamp().isAfter(startTimestamp)
                    && endTimestamp.isAfter(purchase.getPurchaseTimestamp())) {
                filteredPurchases[counter++] = purchase;
            }
        }

        if (counter == 0) {
            return new StockPurchase[0];
        }

        StockPurchase[] finalPurchases = new StockPurchase[counter];
        System.arraycopy(filteredPurchases, 0, finalPurchases, 0, counter);
        return finalPurchases;
    }

    @Override
    public double getNetWorth() {
        if (purchases == null) {
            return 0.0;
        }
        double netWorth = 0.0;
        for (int i = 0; i < sizeStocks; ++i) {
            StockPurchase purchase = purchases[i];
            netWorth += purchase.getQuantity() * priceChart.getCurrentPrice(purchase.getStockTicker());
        }
        return Math.round(netWorth * 100) / 100.0;
    }

    @Override
    public double getRemainingBudget() {
        return Math.round(budget * 100) / 100.0;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public static void main(String[] args) {
        LocalDateTime ts0 = LocalDateTime.now();
        LocalDateTime ts1 = LocalDateTime.now();
        PriceChart priceChart = new PriceChart(10, 20, 30);
        System.out.println(priceChart.getCurrentPrice(null));
        Portfolio myPortfolio = new Portfolio("Plamen", priceChart, 100, 9);
        myPortfolio.buyStock("GOOG", 5);
        myPortfolio.buyStock("AMZ", 2);
        System.out.println(myPortfolio.getNetWorth());
        LocalDateTime ts2 = LocalDateTime.parse("2023-10-28T15:32:56.000");

        myPortfolio = new Portfolio(myPortfolio.getOwner(), priceChart, myPortfolio.getAllPurchases(), 300, 12);
        myPortfolio.buyStock("AMZ", 3);
        myPortfolio.buyStock("AMZ", 2);
        myPortfolio.buyStock("AMZ", 1);
        System.out.println(myPortfolio.getNetWorth());

        StockPurchase[] purchasesInterval = myPortfolio.getAllPurchases(ts0, ts1);
    }
}
