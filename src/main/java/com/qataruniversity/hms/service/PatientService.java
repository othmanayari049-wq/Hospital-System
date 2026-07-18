package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class PatientService {
    private final CrudRepository<Patient> patients;

    public PatientService(CrudRepository<Patient> patients) {
        this.patients = patients;
    }

    public Patient register(Patient patient) {
        boolean duplicateMrn = patients.findAll().stream()
                .anyMatch(existing -> existing.getMedicalRecordNumber().equalsIgnoreCase(patient.getMedicalRecordNumber()));
        if (duplicateMrn) throw new IllegalArgumentException("medical record number already exists");
        return patients.save(patient);
    }

    public Patient get(String id) {
        return patients.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient", id));
    }

    public List<Patient> list() {
        return patients.findAll().stream().sorted(Comparator.comparing(Patient::getFullName)).toList();
    }

    public List<Patient> search(String query) {
        String normalized = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
        return list().stream().filter(patient ->
                patient.getFullName().toLowerCase(Locale.ROOT).contains(normalized)
                || patient.getMedicalRecordNumber().toLowerCase(Locale.ROOT).contains(normalized)
                || patient.getPhone().toLowerCase(Locale.ROOT).contains(normalized)
        ).toList();
    }
}
