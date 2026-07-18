package com.qataruniversity.hms.domain;

import java.math.BigDecimal;

public final class InventoryItem implements Identifiable {
    private final String id;
    private String name;
    private String category;
    private int quantity;
    private int reorderLevel;
    private BigDecimal unitPrice;

    public InventoryItem(String id, String name, String category, int quantity,
                         int reorderLevel, BigDecimal unitPrice) {
        this.id = Person.requireText(id, "inventory id");
        this.name = Person.requireText(name, "inventory name");
        this.category = Person.requireText(category, "inventory category");
        if (quantity < 0 || reorderLevel < 0) throw new IllegalArgumentException("stock values cannot be negative");
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.unitPrice = Staff.requireNonNegative(unitPrice, "unit price");
    }

    @Override public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public int getReorderLevel() { return reorderLevel; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public boolean isLowStock() { return quantity <= reorderLevel; }

    public void addStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("stock amount must be positive");
        quantity += amount;
    }

    public void removeStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("stock amount must be positive");
        if (amount > quantity) throw new IllegalArgumentException("insufficient stock");
        quantity -= amount;
    }
}
