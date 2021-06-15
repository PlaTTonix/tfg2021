package com.tfg.campus.model;

public class Order {
    private String amount;
    private String content;
    private String date;
    private String oid;
    private String status;
    private String uid;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Order(String amount, String content, String date, String oid, String status, String uid) {
        this.amount = amount;
        this.content = content;
        this.date = date;
        this.oid = oid;
        this.status = status;
        this.uid = uid;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return oid + " - " + date + " - " + status;
    }
}
