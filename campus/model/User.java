package com.tfg.campus.model;

public class User {
    private String uid;
    private String cash;
    private String dni;
    private String email;
    private String name;
    private String phone;
    private String room;
    private String surname;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User() {
    }

    public User(String cash, String dni, String email, String name, String phone, String room, String surname, String uid) {
        this.uid = uid;
        this.cash = cash;
        this.dni = dni;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.room = room;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return name;
    }
}