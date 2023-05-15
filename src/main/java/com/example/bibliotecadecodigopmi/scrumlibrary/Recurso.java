package com.example.bibliotecadecodigopmi.scrumlibrary;

public class Recurso {
    private String name;
    private double costPerHour;

    public Recurso(String name, double costPerHour) {
        this.name = name;
        this.costPerHour = costPerHour;
    }

    public String getName() {
        return name;
    }

    public double getCostPerHour() {
        return costPerHour;
    }
}

