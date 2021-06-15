package com.tfg.campus.model;

public class UserAppointment {
    private String uid;
    private String did;
    private String symptoms;

    public UserAppointment(String did, String symptoms, String uid) {
        this.did = did;
        this.symptoms = symptoms;
        this.uid = uid;
    }

    public UserAppointment() {
    }

    public String getuID() {
        return uid;
    }

    public void setuID(String uID) {
        this.uid = uID;
    }

    public String getdID() {
        return did;
    }

    public void setdID(String dID) {
        this.did = dID;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "UsersAppointments{" +
                "uid='" + uid + '\'' +
                ", did='" + did + '\'' +
                ", symptoms='" + symptoms + '\'' +
                '}';
    }
}
