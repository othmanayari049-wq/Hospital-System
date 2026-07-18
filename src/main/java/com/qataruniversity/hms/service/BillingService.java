package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Bill;
import com.qataruniversity.hms.domain.BillItem;
import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.domain.Payment;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public final class BillingService {
    private final CrudRepository<Bill> bills;
    private final CrudRepository<Payment> payments;
    private final CrudRepository<Patient> patients;

    public BillingService(CrudRepository<Bill> bills,
                          CrudRepository<Payment> payments,
                          CrudRepository<Patient> patients) {
        this.bills = bills;
        this.payments = payments;
        this.patients = patients;
    }

    public Bill create(Bill bill) {
        requirePatient(bill.getPatientId());
        return bills.save(bill);
    }

    public Bill addItem(String billId, BillItem item) {
        Bill bill = get(billId);
        bill.addItem(item);
        return bills.save(bill);
    }

    public Payment recordPayment(Payment payment) {
        Bill bill = get(payment.getBillId());
        bill.applyPayment(payment.getAmount());
        bills.save(bill);
        return payments.save(payment);
    }

    public Bill get(String id) {
        return bills.findById(id).orElseThrow(() -> new EntityNotFoundException("Bill", id));
    }

    public List<Bill> list() {
        return bills.findAll().stream().sorted(Comparator.comparing(Bill::getCreatedAt).reversed()).toList();
    }

    public List<Bill> forPatient(String patientId) {
        requirePatient(patientId);
        return list().stream().filter(bill -> bill.getPatientId().equals(patientId)).toList();
    }

    public BigDecimal totalOutstanding() {
        return bills.findAll().stream().map(Bill::getOutstandingAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Patient requirePatient(String id) {
        return patients.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient", id));
    }
}
