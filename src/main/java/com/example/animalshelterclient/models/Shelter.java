package com.example.animalshelterclient.models;

public class Shelter {
    private long id;
    private String name;
    private String location;
    private int availableBeds;

    // Konstruktor med name och availableBeds
    public Shelter(String name, int availableBeds) {
        this.name = name;
        this.availableBeds = availableBeds;
    }

    // Konstruktor med name, location och availableBeds
    public Shelter(String name, String location, int availableBeds) {
        this.name = name;
        this.location = location;
        this.availableBeds = availableBeds;
    }

    // Standardkonstruktor (behövs för exempelvis deserialisering)
    public Shelter() {
    }

    // Getters och setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(int availableBeds) {
        this.availableBeds = availableBeds;
    }

    // För att enkelt skriva ut objektet (om du vill)
    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", availableBeds=" + availableBeds +
                '}';
    }
}
