package com.qataruniversity.hms.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public abstract class Person implements Identifiable {
    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;

    protected Person(String id, String firstName, String lastName, LocalDate dateOfBirth,
                     Gender gender, String phone, String email) {
        this.id = requireText(id, "id");
        this.firstName = requireText(firstName, "first name");
        this.lastName = requireText(lastName, "last name");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "date of birth is required");
        this.gender = Objects.requireNonNullElse(gender, Gender.NOT_SPECIFIED);
        setPhone(phone);
        setEmail(email);
    }

    @Override
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = requireText(firstName, "first name"); }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = requireText(lastName, "last name"); }
    public String getFullName() { return firstName + " " + lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = Objects.requireNonNull(dateOfBirth); }
    public int getAge() { return Period.between(dateOfBirth, LocalDate.now()).getYears(); }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = Objects.requireNonNullElse(gender, Gender.NOT_SPECIFIED); }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone == null ? "" : phone.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? "" : email.trim().toLowerCase(); }

    protected static String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        return value.trim();
    }
}
