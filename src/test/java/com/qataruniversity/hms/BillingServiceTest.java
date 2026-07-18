package com.qataruniversity.hms;

import com.qataruniversity.hms.config.SeedData;
import com.qataruniversity.hms.domain.Bill;
import com.qataruniversity.hms.domain.Payment;
import com.qataruniversity.hms.domain.PaymentStatus;
import com.qataruniversity.hms.service.HospitalSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingServiceTest {
    private HospitalSystem system;

    @BeforeEach
    void setUp() {
        system = new HospitalSystem();
        SeedData.load(system);
    }

    @Test
    void tracksPartialAndFullPayments() {
        Bill bill = system.billingService.get("BIL-001");
        assertEquals(PaymentStatus.PARTIALLY_PAID, bill.getStatus());

        system.billingService.recordPayment(new Payment("PAY-X", bill.getId(), bill.getOutstandingAmount(),
                LocalDateTime.now(), "Card", "TEST"));

        assertEquals(PaymentStatus.PAID, bill.getStatus());
        assertEquals(new BigDecimal("0.00"), bill.getOutstandingAmount());
    }
}
