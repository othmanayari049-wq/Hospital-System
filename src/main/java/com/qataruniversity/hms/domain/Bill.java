package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Bill implements Identifiable, Payable {
    private final String id;
    private final String patientId;
    private final LocalDateTime createdAt;
    private final List<BillItem> items = new ArrayList<>();
    private BigDecimal paidAmount = BigDecimal.ZERO.setScale(2);
    private PaymentStatus status = PaymentStatus.UNPAID;

    public Bill(String id, String patientId, LocalDateTime createdAt) {
        this.id = Person.requireText(id, "bill id");
        this.patientId = Person.requireText(patientId, "patient id");
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    @Override public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public PaymentStatus getStatus() { return status; }
    public List<BillItem> getItems() { return Collections.unmodifiableList(items); }

    public void addItem(BillItem item) {
        if (status == PaymentStatus.VOID) throw new IllegalStateException("void bill cannot be changed");
        items.add(item);
        refreshStatus();
    }

    @Override
    public BigDecimal getTotalAmount() {
        return items.stream().map(BillItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override public BigDecimal getPaidAmount() { return paidAmount; }

    public void applyPayment(BigDecimal amount) {
        amount = Staff.requireNonNegative(amount, "payment amount").setScale(2, RoundingMode.HALF_UP);
        if (amount.signum() == 0) throw new IllegalArgumentException("payment must be greater than zero");
        if (amount.compareTo(getOutstandingAmount()) > 0) throw new IllegalArgumentException("payment exceeds outstanding amount");
        paidAmount = paidAmount.add(amount);
        refreshStatus();
    }

    public void voidBill() {
        if (paidAmount.signum() > 0) throw new IllegalStateException("paid bill cannot be voided");
        status = PaymentStatus.VOID;
    }

    private void refreshStatus() {
        if (status == PaymentStatus.VOID) return;
        if (getTotalAmount().signum() == 0 || paidAmount.signum() == 0) status = PaymentStatus.UNPAID;
        else if (paidAmount.compareTo(getTotalAmount()) >= 0) status = PaymentStatus.PAID;
        else status = PaymentStatus.PARTIALLY_PAID;
    }
}
