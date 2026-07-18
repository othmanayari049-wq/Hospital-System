package com.qataruniversity.hms.domain;

import java.math.BigDecimal;

public final class Room implements Identifiable {
    private final String id;
    private final String roomNumber;
    private RoomType type;
    private BigDecimal dailyRate;
    private String activeAdmissionId;

    public Room(String id, String roomNumber, RoomType type, BigDecimal dailyRate) {
        this.id = Person.requireText(id, "room id");
        this.roomNumber = Person.requireText(roomNumber, "room number");
        this.type = type == null ? RoomType.GENERAL : type;
        this.dailyRate = Staff.requireNonNegative(dailyRate, "daily rate");
    }

    @Override public String getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type == null ? RoomType.GENERAL : type; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = Staff.requireNonNegative(dailyRate, "daily rate"); }
    public String getActiveAdmissionId() { return activeAdmissionId; }
    public boolean isAvailable() { return activeAdmissionId == null; }

    public void occupy(String admissionId) {
        if (!isAvailable()) throw new IllegalStateException("room is already occupied");
        activeAdmissionId = Person.requireText(admissionId, "admission id");
    }

    public void release() { activeAdmissionId = null; }
}
