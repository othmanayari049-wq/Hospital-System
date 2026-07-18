package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Appointment;
import com.qataruniversity.hms.domain.AppointmentStatus;
import com.qataruniversity.hms.domain.Doctor;
import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.exception.SchedulingConflictException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public final class AppointmentService {
    private final CrudRepository<Appointment> appointments;
    private final CrudRepository<Patient> patients;
    private final CrudRepository<Doctor> doctors;

    public AppointmentService(CrudRepository<Appointment> appointments,
                              CrudRepository<Patient> patients,
                              CrudRepository<Doctor> doctors) {
        this.appointments = appointments;
        this.patients = patients;
        this.doctors = doctors;
    }

    public Appointment schedule(Appointment appointment) {
        requirePatient(appointment.getPatientId());
        requireDoctor(appointment.getDoctorId());
        ensureNoConflict(appointment, null);
        return appointments.save(appointment);
    }

    public Appointment reschedule(String appointmentId, LocalDateTime start, Duration duration) {
        Appointment appointment = get(appointmentId);
        Appointment candidate = new Appointment("candidate", appointment.getPatientId(), appointment.getDoctorId(),
                start, duration, appointment.getReason());
        ensureNoConflict(candidate, appointmentId);
        appointment.reschedule(start, duration);
        return appointments.save(appointment);
    }

    public Appointment cancel(String id, String reason) {
        Appointment appointment = get(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setNotes(reason);
        return appointments.save(appointment);
    }

    public Appointment complete(String id, String notes) {
        Appointment appointment = get(id);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setNotes(notes);
        return appointments.save(appointment);
    }

    public Appointment get(String id) {
        return appointments.findById(id).orElseThrow(() -> new EntityNotFoundException("Appointment", id));
    }

    public List<Appointment> list() {
        return appointments.findAll().stream().sorted(Comparator.comparing(Appointment::getStartTime)).toList();
    }

    public List<Appointment> onDate(LocalDate date) {
        return list().stream().filter(a -> a.getStartTime().toLocalDate().equals(date)).toList();
    }

    public List<Appointment> forPatient(String patientId) {
        requirePatient(patientId);
        return list().stream().filter(a -> a.getPatientId().equals(patientId)).toList();
    }

    private void ensureNoConflict(Appointment candidate, String ignoredId) {
        for (Appointment existing : appointments.findAll()) {
            if (existing.getId().equals(ignoredId)) continue;
            if (existing.getStatus() == AppointmentStatus.CANCELLED) continue;
            if (!candidate.overlaps(existing)) continue;
            if (candidate.getDoctorId().equals(existing.getDoctorId())) {
                throw new SchedulingConflictException("doctor already has an appointment in this time slot");
            }
            if (candidate.getPatientId().equals(existing.getPatientId())) {
                throw new SchedulingConflictException("patient already has an appointment in this time slot");
            }
        }
    }

    private Patient requirePatient(String id) {
        return patients.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient", id));
    }

    private Doctor requireDoctor(String id) {
        return doctors.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor", id));
    }
}
