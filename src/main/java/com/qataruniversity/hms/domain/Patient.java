package com.qataruniversity.hms.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Patient extends Person {
    private final String medicalRecordNumber;
    private BloodType bloodType;
    private String emergencyContact;
    private String insuranceProvider;
    private final List<String> allergies = new ArrayList<>();

    public Patient(String id, String medicalRecordNumber, String firstName, String lastName,
                   LocalDate dateOfBirth, Gender gender, String phone, String email,
                   BloodType bloodType, String emergencyContact, String insuranceProvider) {
        super(id, firstName, lastName, dateOfBirth, gender, phone, email);
        this.medicalRecordNumber = requireText(medicalRecordNumber, "medical record number");
        this.bloodType = bloodType == null ? BloodType.UNKNOWN : bloodType;
        this.emergencyContact = emergencyContact == null ? "" : emergencyContact.trim();
        this.insuranceProvider = insuranceProvider == null ? "Self-pay" : insuranceProvider.trim();
    }

    public String getMedicalRecordNumber() { return medicalRecordNumber; }
    public BloodType getBloodType() { return bloodType; }
    public void setBloodType(BloodType bloodType) { this.bloodType = bloodType == null ? BloodType.UNKNOWN : bloodType; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact == null ? "" : emergencyContact.trim(); }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider == null ? "Self-pay" : insuranceProvider.trim(); }
    public List<String> getAllergies() { return Collections.unmodifiableList(allergies); }

    public void addAllergy(String allergy) {
        if (allergy != null && !allergy.isBlank() && allergies.stream().noneMatch(a -> a.equalsIgnoreCase(allergy))) {
            allergies.add(allergy.trim());
        }
    }

    public void removeAllergy(String allergy) { allergies.removeIf(a -> a.equalsIgnoreCase(allergy)); }

    @Override
    public String toString() { return medicalRecordNumber + " - " + getFullName() + " (" + getAge() + ")"; }
}
