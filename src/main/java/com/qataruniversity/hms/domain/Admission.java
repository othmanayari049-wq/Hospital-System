package com.qataruniversity.hms.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Admission implements Identifiable {
    private final String id;
    private final String patientId;
    private final String roomId;
    private final LocalDateTime admittedAt;
    private LocalDateTime dischargedAt;
    private AdmissionStatus status;
    private String reason;

    public Admission(String id, String patientId, String roomId, LocalDateTime admittedAt, String reason) {
        this.id = Person.requireText(id, "admission id");
        this.patientId = Person.requireText(patientId, "patient id");
        this.roomId = Person.requireText(roomId, "room id");
        this.admittedAt = admittedAt == null ? LocalDateTime.now() : admittedAt;
        this.reason = Person.requireText(reason, "admission reason");
        this.status = AdmissionStatus.ACTIVE;
    }

    @Override public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getRoomId() { return roomId; }
    public LocalDateTime getAdmittedAt() { return admittedAt; }
    public LocalDateTime getDischargedAt() { return dischargedAt; }
    public AdmissionStatus getStatus() { return status; }
    public String getReason() { return reason; }

    public void discharge(LocalDateTime at) {
        if (status != AdmissionStatus.ACTIVE) throw new IllegalStateException("admission is not active");
        dischargedAt = at == null ? LocalDateTime.now() : at;
        if (dischargedAt.isBefore(admittedAt)) throw new IllegalArgumentException("discharge cannot be before admission");
        status = AdmissionStatus.DISCHARGED;
    }

    public long getChargeableDays() {
        LocalDateTime end = dischargedAt == null ? LocalDateTime.now() : dischargedAt;
        return Math.max(1, ChronoUnit.DAYS.between(admittedAt.toLocalDate(), end.toLocalDate()) + 1);
    }
}
