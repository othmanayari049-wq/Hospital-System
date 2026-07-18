package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Nurse extends Staff {
    private String shift;
    private String grade;

    public Nurse(String id, String employeeNumber, String firstName, String lastName,
                 LocalDate dateOfBirth, Gender gender, String phone, String email,
                 String departmentId, BigDecimal monthlySalary, String shift, String grade) {
        super(id, employeeNumber, firstName, lastName, dateOfBirth, gender, phone, email,
                departmentId, monthlySalary, StaffRole.NURSE);
        this.shift = requireText(shift, "shift");
        this.grade = requireText(grade, "grade");
    }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = requireText(shift, "shift"); }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = requireText(grade, "grade"); }
}
