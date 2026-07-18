package com.qataruniversity.hms.domain;

import java.math.BigDecimal;

public interface Payable {
    BigDecimal getTotalAmount();
    BigDecimal getPaidAmount();

    default BigDecimal getOutstandingAmount() {
        return getTotalAmount().subtract(getPaidAmount()).max(BigDecimal.ZERO);
    }

    default boolean isFullyPaid() {
        return getOutstandingAmount().signum() == 0;
    }
}
