package com.qataruniversity.hms.config;

import com.qataruniversity.hms.domain.*;
import com.qataruniversity.hms.service.HospitalSystem;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class SeedData {
    private SeedData() { }

    public static void load(HospitalSystem system) {
        system.departments.save(new Department("DEP-001", "Cardiology", "Building A - Floor 2", "2201"));
        system.departments.save(new Department("DEP-002", "General Medicine", "Building A - Floor 1", "2101"));
        system.departments.save(new Department("DEP-003", "Pediatrics", "Building B - Floor 1", "3101"));

        Doctor doctor1 = new Doctor("DOC-001", "EMP-1001", "Sara", "Al-Kuwari",
                LocalDate.of(1983, 5, 18), Gender.FEMALE, "+974 5500 1001", "sara@hospital.test",
                "DEP-001", new BigDecimal("32000"), "Cardiology", "LIC-QA-1001", new BigDecimal("350"));
        Doctor doctor2 = new Doctor("DOC-002", "EMP-1002", "Omar", "Hassan",
                LocalDate.of(1979, 9, 4), Gender.MALE, "+974 5500 1002", "omar@hospital.test",
                "DEP-002", new BigDecimal("29000"), "Internal Medicine", "LIC-QA-1002", new BigDecimal("300"));
        system.doctors.save(doctor1);
        system.doctors.save(doctor2);

        system.nurses.save(new Nurse("NUR-001", "EMP-2001", "Mariam", "Ali",
                LocalDate.of(1992, 3, 11), Gender.FEMALE, "+974 5500 2001", "mariam@hospital.test",
                "DEP-002", new BigDecimal("12500"), "Morning", "Senior Nurse"));

        Patient patient1 = new Patient("PAT-001", "MRN-2026-0001", "Ahmed", "Nasser",
                LocalDate.of(1990, 6, 12), Gender.MALE, "+974 6600 1001", "ahmed@example.test",
                BloodType.O_POSITIVE, "+974 6600 9001", "Qatar Health Plan");
        patient1.addAllergy("Penicillin");
        Patient patient2 = new Patient("PAT-002", "MRN-2026-0002", "Layla", "Mohamed",
                LocalDate.of(2001, 2, 23), Gender.FEMALE, "+974 6600 1002", "layla@example.test",
                BloodType.A_POSITIVE, "+974 6600 9002", "Self-pay");
        Patient patient3 = new Patient("PAT-003", "MRN-2026-0003", "Yousef", "Khalid",
                LocalDate.of(2014, 10, 5), Gender.MALE, "+974 6600 1003", "parent@example.test",
                BloodType.B_POSITIVE, "+974 6600 9003", "Family Insurance");
        system.patientService.register(patient1);
        system.patientService.register(patient2);
        system.patientService.register(patient3);

        system.rooms.save(new Room("ROM-001", "A-101", RoomType.GENERAL, new BigDecimal("700")));
        system.rooms.save(new Room("ROM-002", "A-102", RoomType.PRIVATE, new BigDecimal("1200")));
        system.rooms.save(new Room("ROM-003", "ICU-01", RoomType.ICU, new BigDecimal("3500")));
        system.rooms.save(new Room("ROM-004", "P-201", RoomType.PEDIATRIC, new BigDecimal("900")));

        LocalDateTime today10 = LocalDate.now().atTime(10, 0);
        system.appointmentService.schedule(new Appointment("APT-001", patient1.getId(), doctor1.getId(),
                today10, Duration.ofMinutes(30), "Cardiology follow-up"));
        system.appointmentService.schedule(new Appointment("APT-002", patient2.getId(), doctor2.getId(),
                today10.plusHours(1), Duration.ofMinutes(45), "General consultation"));

        MedicalRecord record = new MedicalRecord("REC-001", patient1.getId(), doctor1.getId(),
                LocalDateTime.now().minusDays(30), "Routine follow-up", "Continue current plan",
                "Educational demonstration record only");
        record.addObservation("Blood pressure", "120/80");
        system.medicalRecordService.addRecord(record);

        Prescription prescription = new Prescription("PRE-001", patient1.getId(), doctor1.getId(),
                LocalDateTime.now().minusDays(30), "Take after food");
        prescription.addItem(new PrescriptionItem("Example Medication", "10 mg", "Once daily", 14));
        system.medicalRecordService.issuePrescription(prescription);

        Bill bill = new Bill("BIL-001", patient1.getId(), LocalDateTime.now());
        system.billingService.create(bill);
        system.billingService.addItem(bill.getId(), new BillItem("Consultation", 1, doctor1.getConsultationFee()));
        system.billingService.addItem(bill.getId(), new BillItem("ECG service", 1, new BigDecimal("180")));
        system.billingService.recordPayment(new Payment("PAY-001", bill.getId(), new BigDecimal("200"),
                LocalDateTime.now(), "Card", "DEMO-REF-001"));

        system.inventoryService.add(new InventoryItem("INV-001", "Surgical gloves", "Consumables", 250, 80, new BigDecimal("0.80")));
        system.inventoryService.add(new InventoryItem("INV-002", "Face masks", "Consumables", 70, 100, new BigDecimal("0.45")));
        system.inventoryService.add(new InventoryItem("INV-003", "Saline 500 ml", "Pharmacy", 45, 20, new BigDecimal("8.50")));
    }
}
