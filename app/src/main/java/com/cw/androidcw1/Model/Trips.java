package com.cw.androidcw1.Model;

import java.io.Serializable;

public class Trips implements Serializable {
    private int id;
    private String name;
    private String destination;
    private String date;
    private String requireAssessement;
    private String description;

    public Trips() {
    }

    public Trips(int id, String name, String destination, String date, String requireAssessement, String description) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.requireAssessement = requireAssessement;
        this.description = description;
    }

    public Trips(String name, String destination, String date, String requireAssessement, String description) {
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.requireAssessement = requireAssessement;
        this.description = description;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequireAssessement() {
        return requireAssessement;
    }

    public void setRequireAssessement(String requireAssessement) {
        this.requireAssessement = requireAssessement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
