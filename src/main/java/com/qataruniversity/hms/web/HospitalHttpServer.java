package com.qataruniversity.hms.web;

import com.qataruniversity.hms.domain.*;
import com.qataruniversity.hms.service.HospitalSystem;
import com.qataruniversity.hms.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public final class HospitalHttpServer {
    private final HospitalSystem system;
    private final HttpServer server;

    public HospitalHttpServer(HospitalSystem system, int port) throws IOException {
        this.system = system;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(6));
        registerRoutes();
    }

    public void start() { server.start(); }
    public void stop() { server.stop(1); }

    private void registerRoutes() {
        server.createContext("/api/dashboard", json(exchange -> system.dashboardService.summary()));
        server.createContext("/api/patients", json(exchange -> system.patientService.list().stream().map(this::patientMap).toList()));
        server.createContext("/api/doctors", json(exchange -> system.doctors.findAll().stream().map(this::doctorMap).toList()));
        server.createContext("/api/appointments", json(exchange -> system.appointmentService.list().stream().map(this::appointmentMap).toList()));
        server.createContext("/api/rooms", json(exchange -> system.rooms.findAll().stream().map(this::roomMap).toList()));
        server.createContext("/api/bills", json(exchange -> system.billingService.list().stream().map(this::billMap).toList()));
        server.createContext("/api/inventory", json(exchange -> system.inventoryService.list().stream().map(this::inventoryMap).toList()));
        server.createContext("/", this::serveStatic);
    }

    private HttpHandler json(JsonSupplier supplier) {
        return exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "application/json", "{\"error\":\"Method not allowed\"}");
                return;
            }
            try {
                send(exchange, 200, "application/json", JsonUtil.toJson(supplier.get(exchange)));
            } catch (Exception error) {
                send(exchange, 500, "application/json", JsonUtil.toJson(Map.of("error", error.getMessage())));
            }
        };
    }

    private void serveStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";
        if (path.contains("..")) {
            send(exchange, 400, "text/plain", "Invalid path");
            return;
        }
        try (InputStream input = HospitalHttpServer.class.getResourceAsStream("/web" + path)) {
            if (input == null) {
                send(exchange, 404, "text/plain", "Not found");
                return;
            }
            String type = path.endsWith(".css") ? "text/css" : path.endsWith(".js") ? "application/javascript" : "text/html";
            byte[] body = input.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", type + "; charset=utf-8");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        }
    }

    private void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private Map<String, Object> patientMap(Patient p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId()); m.put("mrn", p.getMedicalRecordNumber()); m.put("name", p.getFullName());
        m.put("age", p.getAge()); m.put("gender", p.getGender()); m.put("bloodType", p.getBloodType().getDisplayName());
        m.put("phone", p.getPhone()); m.put("insurance", p.getInsuranceProvider());
        return m;
    }

    private Map<String, Object> doctorMap(Doctor d) {
        return Map.of("id", d.getId(), "name", d.toString(), "specialization", d.getSpecialization(), "fee", d.getConsultationFee());
    }

    private Map<String, Object> appointmentMap(Appointment a) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", a.getId()); m.put("patientId", a.getPatientId()); m.put("doctorId", a.getDoctorId());
        m.put("startTime", a.getStartTime()); m.put("endTime", a.getEndTime()); m.put("reason", a.getReason()); m.put("status", a.getStatus());
        return m;
    }

    private Map<String, Object> roomMap(Room r) {
        return Map.of("id", r.getId(), "number", r.getRoomNumber(), "type", r.getType(), "dailyRate", r.getDailyRate(), "available", r.isAvailable());
    }

    private Map<String, Object> billMap(Bill b) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", b.getId()); m.put("patientId", b.getPatientId()); m.put("createdAt", b.getCreatedAt());
        m.put("total", b.getTotalAmount()); m.put("paid", b.getPaidAmount()); m.put("outstanding", b.getOutstandingAmount()); m.put("status", b.getStatus());
        return m;
    }

    private Map<String, Object> inventoryMap(InventoryItem i) {
        return Map.of("id", i.getId(), "name", i.getName(), "category", i.getCategory(), "quantity", i.getQuantity(), "reorderLevel", i.getReorderLevel(), "lowStock", i.isLowStock());
    }

    @FunctionalInterface
    private interface JsonSupplier {
        Object get(HttpExchange exchange);
    }
}
