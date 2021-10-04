package com.codegym.model;

public class Shipment {
    private int id;

    private String name;

    public Shipment(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Shipment(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
