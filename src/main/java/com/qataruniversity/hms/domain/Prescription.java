package com.qataruniversity.hms.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Prescription implements Identifiable {
    private final String id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime issuedAt;
    private final List<PrescriptionItem> items = new ArrayList<>();
    private String instructions;

    public Prescription(String id, String patientId, String doctorId, LocalDateTime issuedAt, String instructions) {
        this.id = Person.requireText(id, "prescription id");
        this.patientId = Person.requireText(patientId, "patient id");
        this.doctorId = Person.requireText(doctorId, "doctor id");
        this.issuedAt = issuedAt == null ? LocalDateTime.now() : issuedAt;
        this.instructions = instructions == null ? "" : instructions.trim();
    }

    @Override public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions == null ? "" : instructions.trim(); }
    public void addItem(PrescriptionItem item) { items.add(item); }
    public List<PrescriptionItem> getItems() { return Collections.unmodifiableList(items); }
}
