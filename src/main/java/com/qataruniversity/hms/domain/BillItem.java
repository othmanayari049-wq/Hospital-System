package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record BillItem(String description, int quantity, BigDecimal unitPrice) {
    public BillItem {
        description = Person.requireText(description, "bill item description");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        unitPrice = Staff.requireNonNegative(unitPrice, "unit price").setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
