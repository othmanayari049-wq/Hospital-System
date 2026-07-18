package com.qataruniversity.hms.domain;

public final class Department implements Identifiable {
    private final String id;
    private String name;
    private String location;
    private String phoneExtension;

    public Department(String id, String name, String location, String phoneExtension) {
        this.id = Person.requireText(id, "department id");
        this.name = Person.requireText(name, "department name");
        this.location = Person.requireText(location, "department location");
        this.phoneExtension = phoneExtension == null ? "" : phoneExtension.trim();
    }

    @Override public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Person.requireText(name, "department name"); }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = Person.requireText(location, "department location"); }
    public String getPhoneExtension() { return phoneExtension; }
    public void setPhoneExtension(String phoneExtension) { this.phoneExtension = phoneExtension == null ? "" : phoneExtension.trim(); }
}
