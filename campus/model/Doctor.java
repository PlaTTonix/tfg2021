package com.tfg.campus.model;

public class Doctor {
    private String date;
    private String did;
    private String name;
    private String place;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Doctor(String date, String did, String name, String place, String time) {
        this.date = date;
        this.did = did;
        this.name = name;
        this.place = place;
        this.time = time;
    }

    public Doctor() {
    }

    @Override
    public String toString() {
        return name + " - " + date + " - " + time;
    }
}
