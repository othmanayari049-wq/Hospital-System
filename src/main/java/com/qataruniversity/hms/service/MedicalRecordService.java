package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.Doctor;
import com.qataruniversity.hms.domain.MedicalRecord;
import com.qataruniversity.hms.domain.Patient;
import com.qataruniversity.hms.domain.Prescription;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.util.Comparator;
import java.util.List;

public final class MedicalRecordService {
    private final CrudRepository<MedicalRecord> records;
    private final CrudRepository<Prescription> prescriptions;
    private final CrudRepository<Patient> patients;
    private final CrudRepository<Doctor> doctors;

    public MedicalRecordService(CrudRepository<MedicalRecord> records,
                                CrudRepository<Prescription> prescriptions,
                                CrudRepository<Patient> patients,
                                CrudRepository<Doctor> doctors) {
        this.records = records;
        this.prescriptions = prescriptions;
        this.patients = patients;
        this.doctors = doctors;
    }

    public MedicalRecord addRecord(MedicalRecord record) {
        requirePatient(record.getPatientId());
        requireDoctor(record.getDoctorId());
        return records.save(record);
    }

    public Prescription issuePrescription(Prescription prescription) {
        requirePatient(prescription.getPatientId());
        requireDoctor(prescription.getDoctorId());
        if (prescription.getItems().isEmpty()) throw new IllegalArgumentException("prescription requires at least one item");
        return prescriptions.save(prescription);
    }

    public List<MedicalRecord> history(String patientId) {
        requirePatient(patientId);
        return records.findAll().stream()
                .filter(record -> record.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(MedicalRecord::getCreatedAt).reversed())
                .toList();
    }

    public List<Prescription> prescriptions(String patientId) {
        requirePatient(patientId);
        return prescriptions.findAll().stream()
                .filter(item -> item.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(Prescription::getIssuedAt).reversed())
                .toList();
    }

    private Patient requirePatient(String id) {
        return patients.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient", id));
    }

    private Doctor requireDoctor(String id) {
        return doctors.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor", id));
    }
}
