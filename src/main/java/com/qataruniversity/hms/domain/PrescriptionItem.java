package com.qataruniversity.hms.domain;

public record PrescriptionItem(String medicationName, String dosage, String frequency, int durationDays) {
    public PrescriptionItem {
        medicationName = Person.requireText(medicationName, "medication name");
        dosage = Person.requireText(dosage, "dosage");
        frequency = Person.requireText(frequency, "frequency");
        if (durationDays <= 0) {
            throw new IllegalArgumentException("duration days must be positive");
        }
    }
}
