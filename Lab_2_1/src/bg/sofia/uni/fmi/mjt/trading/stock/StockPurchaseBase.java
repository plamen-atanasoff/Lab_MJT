package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public abstract class StockPurchaseBase implements StockPurchase {
    protected int quantity;
    protected LocalDateTime purchaseTimestamp;
    protected double purchasePricePerUnit;

    protected StockPurchaseBase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.purchaseTimestamp = purchaseTimestamp;
        this.quantity = quantity;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return purchasePricePerUnit;
    }

    @Override
    public double getTotalPurchasePrice() {
        return quantity * purchasePricePerUnit;
    }
}
