package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.*;
import com.qataruniversity.hms.repository.InMemoryRepository;

public final class HospitalSystem {
    public final InMemoryRepository<Patient> patients = new InMemoryRepository<>();
    public final InMemoryRepository<Doctor> doctors = new InMemoryRepository<>();
    public final InMemoryRepository<Nurse> nurses = new InMemoryRepository<>();
    public final InMemoryRepository<Department> departments = new InMemoryRepository<>();
    public final InMemoryRepository<Appointment> appointments = new InMemoryRepository<>();
    public final InMemoryRepository<MedicalRecord> medicalRecords = new InMemoryRepository<>();
    public final InMemoryRepository<Prescription> prescriptions = new InMemoryRepository<>();
    public final InMemoryRepository<Room> rooms = new InMemoryRepository<>();
    public final InMemoryRepository<Admission> admissions = new InMemoryRepository<>();
    public final InMemoryRepository<Bill> bills = new InMemoryRepository<>();
    public final InMemoryRepository<Payment> payments = new InMemoryRepository<>();
    public final InMemoryRepository<InventoryItem> inventory = new InMemoryRepository<>();

    public final PatientService patientService = new PatientService(patients);
    public final AppointmentService appointmentService = new AppointmentService(appointments, patients, doctors);
    public final MedicalRecordService medicalRecordService = new MedicalRecordService(
            medicalRecords, prescriptions, patients, doctors);
    public final AdmissionService admissionService = new AdmissionService(admissions, patients, rooms);
    public final BillingService billingService = new BillingService(bills, payments, patients);
    public final InventoryService inventoryService = new InventoryService(inventory);
    public final DashboardService dashboardService = new DashboardService(
            patients, doctors, appointments, rooms, bills, inventory,
            appointmentService, admissionService, billingService, inventoryService);
}
