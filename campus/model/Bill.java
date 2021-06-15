package com.tfg.campus.model;

public class Bill {
    private String amount;
    private String bid;
    private String date;
    private String description;
    private String monthnumber;
    private String uid;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonthnumber() {
        return monthnumber;
    }

    public void setMonthnumber(String monthnumber) {
        this.monthnumber = monthnumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Bill(String amount, String bid, String date, String description, String monthnumber, String uid) {
        this.amount = amount;
        this.bid = bid;
        this.date = date;
        this.description = description;
        this.monthnumber = monthnumber;
        this.uid = uid;
    }

    public Bill() {
    }

    @Override
    public String toString() {
        String monthAmount = null;
        switch (Integer.parseInt(monthnumber)){
            case 1: monthAmount = "Enero" + " - " + amount + "€"; break;
            case 2: monthAmount = "Febrero" + " - " + amount + "€"; break;
            case 3: monthAmount = "Marzo" + " - " + amount + "€"; break;
            case 4: monthAmount = "Abril" + " - " + amount + "€"; break;
            case 5: monthAmount = "Mayo" + " - " + amount + "€"; break;
            case 6: monthAmount = "Junio" + " - " + amount + "€"; break;
            case 7: monthAmount = "Julio" + " - " + amount + "€"; break;
            case 8: monthAmount = "Agosto" + " - " + amount + "€"; break;
            case 9: monthAmount = "Septiembre" + " - " + amount + "€"; break;
            case 10: monthAmount = "Octubre" + " - " + amount + "€"; break;
            case 11: monthAmount = "Noviembre" + " - " + amount + "€"; break;
            case 12: monthAmount = "Diciembre" + " - " + amount + "€"; break;
        }
        return monthAmount;
    }
}
