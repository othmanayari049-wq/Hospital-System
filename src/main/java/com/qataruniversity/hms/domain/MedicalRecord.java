package com.qataruniversity.hms.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MedicalRecord implements Identifiable {
    private final String id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime createdAt;
    private String diagnosis;
    private String treatment;
    private String notes;
    private final Map<String, String> observations = new LinkedHashMap<>();

    public MedicalRecord(String id, String patientId, String doctorId, LocalDateTime createdAt,
                         String diagnosis, String treatment, String notes) {
        this.id = Person.requireText(id, "record id");
        this.patientId = Person.requireText(patientId, "patient id");
        this.doctorId = Person.requireText(doctorId, "doctor id");
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.diagnosis = Person.requireText(diagnosis, "diagnosis");
        this.treatment = treatment == null ? "" : treatment.trim();
        this.notes = notes == null ? "" : notes.trim();
    }

    @Override public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = Person.requireText(diagnosis, "diagnosis"); }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment == null ? "" : treatment.trim(); }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes == null ? "" : notes.trim(); }

    public void addObservation(String name, String value) {
        observations.put(Person.requireText(name, "observation name"), value == null ? "" : value.trim());
    }

    public Map<String, String> getObservations() {
        return Collections.unmodifiableMap(observations);
    }
}
