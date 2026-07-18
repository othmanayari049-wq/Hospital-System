package com.qataruniversity.hms;

import com.qataruniversity.hms.config.SeedData;
import com.qataruniversity.hms.domain.Appointment;
import com.qataruniversity.hms.exception.SchedulingConflictException;
import com.qataruniversity.hms.service.HospitalSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentServiceTest {
    private HospitalSystem system;

    @BeforeEach
    void setUp() {
        system = new HospitalSystem();
        SeedData.load(system);
    }

    @Test
    void rejectsDoctorTimeConflict() {
        Appointment conflicting = new Appointment("APT-X", "PAT-003", "DOC-001",
                LocalDate.now().atTime(10, 10), Duration.ofMinutes(20), "Conflicting slot");
        assertThrows(SchedulingConflictException.class, () -> system.appointmentService.schedule(conflicting));
    }
}
