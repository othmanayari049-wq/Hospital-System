package com.qataruniversity.hms.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Staff extends Person {
    private final String employeeNumber;
    private String departmentId;
    private BigDecimal monthlySalary;
    private final StaffRole role;

    protected Staff(String id, String employeeNumber, String firstName, String lastName,
                    LocalDate dateOfBirth, Gender gender, String phone, String email,
                    String departmentId, BigDecimal monthlySalary, StaffRole role) {
        super(id, firstName, lastName, dateOfBirth, gender, phone, email);
        this.employeeNumber = requireText(employeeNumber, "employee number");
        this.departmentId = requireText(departmentId, "department id");
        this.monthlySalary = requireNonNegative(monthlySalary, "monthly salary");
        this.role = role;
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = requireText(departmentId, "department id"); }
    public BigDecimal getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(BigDecimal monthlySalary) { this.monthlySalary = requireNonNegative(monthlySalary, "monthly salary"); }
    public StaffRole getRole() { return role; }

    protected static BigDecimal requireNonNegative(BigDecimal value, String field) {
        if (value == null || value.signum() < 0) throw new IllegalArgumentException(field + " must be non-negative");
        return value;
    }
}
