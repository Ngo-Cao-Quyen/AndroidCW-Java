package com.cw.androidcw1.Model;

public class Expenses implements java.io.Serializable {
    private int id;
    private int tripId;
    private String type;
    private double amount;
    private String time;

    public Expenses() {
    }

    public Expenses(int id, int tripId, String type, double amount, String time) {
        this.id = id;
        this.tripId = tripId;
        this.type = type;
        this.amount = amount;
        this.time = time;
    }

public Expenses(int tripId, String type, double amount, String time) {
        this.tripId = tripId;
        this.type = type;
        this.amount = amount;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
