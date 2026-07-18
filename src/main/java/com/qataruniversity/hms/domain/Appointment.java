package com.qataruniversity.hms.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Appointment implements Identifiable {
    private final String id;
    private final String patientId;
    private final String doctorId;
    private LocalDateTime startTime;
    private Duration duration;
    private String reason;
    private AppointmentStatus status;
    private String notes;

    public Appointment(String id, String patientId, String doctorId, LocalDateTime startTime,
                       Duration duration, String reason) {
        this.id = Person.requireText(id, "appointment id");
        this.patientId = Person.requireText(patientId, "patient id");
        this.doctorId = Person.requireText(doctorId, "doctor id");
        this.startTime = Objects.requireNonNull(startTime, "start time is required");
        this.duration = requirePositive(duration);
        this.reason = Person.requireText(reason, "appointment reason");
        this.status = AppointmentStatus.SCHEDULED;
        this.notes = "";
    }

    @Override public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getStartTime() { return startTime; }
    public Duration getDuration() { return duration; }
    public LocalDateTime getEndTime() { return startTime.plus(duration); }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = Person.requireText(reason, "appointment reason"); }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = Objects.requireNonNull(status); }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes == null ? "" : notes.trim(); }

    public void reschedule(LocalDateTime newStartTime, Duration newDuration) {
        if (status == AppointmentStatus.COMPLETED || status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("completed or cancelled appointments cannot be rescheduled");
        }
        startTime = Objects.requireNonNull(newStartTime);
        duration = requirePositive(newDuration);
    }

    public boolean overlaps(Appointment other) {
        return startTime.isBefore(other.getEndTime()) && other.getStartTime().isBefore(getEndTime());
    }

    private static Duration requirePositive(Duration duration) {
        if (duration == null || duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("appointment duration must be positive");
        }
        return duration;
    }
}
