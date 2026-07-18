package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Admission;
import com.qataruniversity.hms.domain.AdmissionStatus;
import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.domain.Room;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public final class AdmissionService {
    private final CrudRepository<Admission> admissions;
    private final CrudRepository<Patient> patients;
    private final CrudRepository<Room> rooms;

    public AdmissionService(CrudRepository<Admission> admissions,
                            CrudRepository<Patient> patients,
                            CrudRepository<Room> rooms) {
        this.admissions = admissions;
        this.patients = patients;
        this.rooms = rooms;
    }

    public Admission admit(Admission admission) {
        requirePatient(admission.getPatientId());
        Room room = requireRoom(admission.getRoomId());
        if (!room.isAvailable()) throw new IllegalStateException("selected room is occupied");
        boolean alreadyAdmitted = admissions.findAll().stream()
                .anyMatch(existing -> existing.getPatientId().equals(admission.getPatientId())
                        && existing.getStatus() == AdmissionStatus.ACTIVE);
        if (alreadyAdmitted) throw new IllegalStateException("patient already has an active admission");
        room.occupy(admission.getId());
        rooms.save(room);
        return admissions.save(admission);
    }

    public Admission discharge(String admissionId, LocalDateTime at) {
        Admission admission = get(admissionId);
        admission.discharge(at);
        Room room = requireRoom(admission.getRoomId());
        room.release();
        rooms.save(room);
        return admissions.save(admission);
    }

    public Admission get(String id) {
        return admissions.findById(id).orElseThrow(() -> new EntityNotFoundException("Admission", id));
    }

    public List<Admission> activeAdmissions() {
        return admissions.findAll().stream()
                .filter(admission -> admission.getStatus() == AdmissionStatus.ACTIVE)
                .sorted(Comparator.comparing(Admission::getAdmittedAt))
                .toList();
    }

    public List<Room> availableRooms() {
        return rooms.findAll().stream().filter(Room::isAvailable)
                .sorted(Comparator.comparing(Room::getRoomNumber)).toList();
    }

    private Patient requirePatient(String id) {
        return patients.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient", id));
    }

    private Room requireRoom(String id) {
        return rooms.findById(id).orElseThrow(() -> new EntityNotFoundException("Room", id));
    }
}
