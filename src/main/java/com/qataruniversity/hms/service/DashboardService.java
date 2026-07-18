package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Appointment;
import com.qataruniversity.hms.domain.Bill;
import com.qataruniversity.hms.domain.Doctor;
import com.qataruniversity.hms.domain.InventoryItem;
import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.domain.Room;
import com.qataruniversity.hms.repository.CrudRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DashboardService {
    private final CrudRepository<Patient> patients;
    private final CrudRepository<Doctor> doctors;
    private final CrudRepository<Appointment> appointments;
    private final CrudRepository<Room> rooms;
    private final CrudRepository<Bill> bills;
    private final CrudRepository<InventoryItem> inventory;
    private final AppointmentService appointmentService;
    private final AdmissionService admissionService;
    private final BillingService billingService;
    private final InventoryService inventoryService;

    public DashboardService(CrudRepository<Patient> patients,
                            CrudRepository<Doctor> doctors,
                            CrudRepository<Appointment> appointments,
                            CrudRepository<Room> rooms,
                            CrudRepository<Bill> bills,
                            CrudRepository<InventoryItem> inventory,
                            AppointmentService appointmentService,
                            AdmissionService admissionService,
                            BillingService billingService,
                            InventoryService inventoryService) {
        this.patients = patients;
        this.doctors = doctors;
        this.appointments = appointments;
        this.rooms = rooms;
        this.bills = bills;
        this.inventory = inventory;
        this.appointmentService = appointmentService;
        this.admissionService = admissionService;
        this.billingService = billingService;
        this.inventoryService = inventoryService;
    }

    public Map<String, Object> summary() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("patients", patients.count());
        data.put("doctors", doctors.count());
        data.put("appointments", appointments.count());
        data.put("appointmentsToday", appointmentService.onDate(LocalDate.now()).size());
        data.put("activeAdmissions", admissionService.activeAdmissions().size());
        data.put("availableRooms", admissionService.availableRooms().size());
        data.put("totalRooms", rooms.count());
        data.put("bills", bills.count());
        data.put("outstandingBalance", billingService.totalOutstanding());
        data.put("inventoryItems", inventory.count());
        data.put("lowStockItems", inventoryService.lowStockItems().size());
        return data;
    }
}
