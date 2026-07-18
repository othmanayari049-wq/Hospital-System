package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Doctor extends Staff {
    private String specialization;
    private final String licenseNumber;
    private BigDecimal consultationFee;

    public Doctor(String id, String employeeNumber, String firstName, String lastName,
                  LocalDate dateOfBirth, Gender gender, String phone, String email,
                  String departmentId, BigDecimal monthlySalary, String specialization,
                  String licenseNumber, BigDecimal consultationFee) {
        super(id, employeeNumber, firstName, lastName, dateOfBirth, gender, phone, email,
                departmentId, monthlySalary, StaffRole.DOCTOR);
        this.specialization = requireText(specialization, "specialization");
        this.licenseNumber = requireText(licenseNumber, "license number");
        this.consultationFee = requireNonNegative(consultationFee, "consultation fee");
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = requireText(specialization, "specialization"); }
    public String getLicenseNumber() { return licenseNumber; }
    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = requireNonNegative(consultationFee, "consultation fee"); }

    @Override
    public String toString() { return "Dr. " + getFullName() + " - " + specialization; }
}
