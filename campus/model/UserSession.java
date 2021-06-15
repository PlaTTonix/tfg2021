package com.tfg.campus.model;

public class UserSession {
    private String sid;
    private String uid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserSession(String sid, String uid) {
        this.sid = sid;
        this.uid = uid;
    }

    public UserSession() {
    }
}
