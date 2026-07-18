package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Payment implements Identifiable {
    private final String id;
    private final String billId;
    private final BigDecimal amount;
    private final LocalDateTime paidAt;
    private final String method;
    private final String reference;

    public Payment(String id, String billId, BigDecimal amount, LocalDateTime paidAt,
                   String method, String reference) {
        this.id = Person.requireText(id, "payment id");
        this.billId = Person.requireText(billId, "bill id");
        this.amount = Staff.requireNonNegative(amount, "payment amount");
        if (this.amount.signum() == 0) throw new IllegalArgumentException("payment amount must be positive");
        this.paidAt = paidAt == null ? LocalDateTime.now() : paidAt;
        this.method = Person.requireText(method, "payment method");
        this.reference = reference == null ? "" : reference.trim();
    }

    @Override public String getId() { return id; }
    public String getBillId() { return billId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public String getMethod() { return method; }
    public String getReference() { return reference; }
}
