package com.qataruniversity.hms.cli;

import com.qataruniversity.hms.domain.*;
import com.qataruniversity.hms.service.HospitalSystem;
import com.qataruniversity.hms.util.IdGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public final class ConsoleMenu {
    private final HospitalSystem system;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(HospitalSystem system) {
        this.system = system;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> showDashboard();
                    case "2" -> listPatients();
                    case "3" -> registerPatient();
                    case "4" -> listAppointments();
                    case "5" -> scheduleAppointment();
                    case "6" -> listRooms();
                    case "7" -> listBills();
                    case "8" -> listInventory();
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (RuntimeException error) {
                System.out.println("Operation failed: " + error.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("=== Hospital Management System ===");
        System.out.println("1. Dashboard");
        System.out.println("2. List patients");
        System.out.println("3. Register patient");
        System.out.println("4. List appointments");
        System.out.println("5. Schedule appointment");
        System.out.println("6. List rooms");
        System.out.println("7. List bills");
        System.out.println("8. List inventory");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private void showDashboard() {
        system.dashboardService.summary().forEach((key, value) -> System.out.printf("%-22s %s%n", key, value));
    }

    private void listPatients() {
        system.patientService.list().forEach(patient -> System.out.println(patient.getId() + " | " + patient));
    }

    private void registerPatient() {
        System.out.print("First name: "); String first = scanner.nextLine();
        System.out.print("Last name: "); String last = scanner.nextLine();
        System.out.print("Birth date (YYYY-MM-DD): "); LocalDate dob = LocalDate.parse(scanner.nextLine());
        System.out.print("Phone: "); String phone = scanner.nextLine();
        String id = IdGenerator.next("PAT");
        String mrn = "MRN-" + LocalDate.now().getYear() + "-" + id.substring(id.indexOf('-') + 1);
        Patient patient = new Patient(id, mrn, first, last, dob, Gender.NOT_SPECIFIED,
                phone, "", BloodType.UNKNOWN, "", "Self-pay");
        system.patientService.register(patient);
        System.out.println("Registered: " + patient);
    }

    private void listAppointments() {
        system.appointmentService.list().forEach(a -> System.out.printf("%s | %s | patient=%s | doctor=%s | %s%n",
                a.getId(), a.getStartTime(), a.getPatientId(), a.getDoctorId(), a.getStatus()));
    }

    private void scheduleAppointment() {
        System.out.print("Patient ID: "); String patientId = scanner.nextLine();
        System.out.print("Doctor ID: "); String doctorId = scanner.nextLine();
        System.out.print("Start (YYYY-MM-DDTHH:MM): "); LocalDateTime start = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Reason: "); String reason = scanner.nextLine();
        Appointment appointment = new Appointment(IdGenerator.next("APT"), patientId, doctorId,
                start, Duration.ofMinutes(30), reason);
        system.appointmentService.schedule(appointment);
        System.out.println("Scheduled: " + appointment.getId());
    }

    private void listRooms() {
        system.rooms.findAll().forEach(room -> System.out.printf("%s | %s | %s | QAR %s/day | %s%n",
                room.getId(), room.getRoomNumber(), room.getType(), room.getDailyRate(),
                room.isAvailable() ? "Available" : "Occupied"));
    }

    private void listBills() {
        system.billingService.list().forEach(bill -> System.out.printf("%s | patient=%s | total=QAR %s | due=QAR %s | %s%n",
                bill.getId(), bill.getPatientId(), bill.getTotalAmount(), bill.getOutstandingAmount(), bill.getStatus()));
    }

    private void listInventory() {
        system.inventoryService.list().forEach(item -> System.out.printf("%s | %s | qty=%d | %s%n",
                item.getId(), item.getName(), item.getQuantity(), item.isLowStock() ? "LOW STOCK" : "OK"));
    }
}
