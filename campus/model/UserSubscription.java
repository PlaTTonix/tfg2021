package com.tfg.campus.model;

public class UserSubscription {
    private String subid1;
    private String subid2;
    private String subid3;
    private String uid;

    public String getSubid1() {
        return subid1;
    }

    public void setSubid1(String subid1) {
        this.subid1 = subid1;
    }

    public String getSubid2() {
        return subid2;
    }

    public void setSubid2(String subid2) {
        this.subid2 = subid2;
    }

    public String getSubid3() {
        return subid3;
    }

    public void setSubid3(String subid3) {
        this.subid3 = subid3;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserSubscription() {
    }

    @Override
    public String toString() {
        return "UserSubscription{" +
                "subid1='" + subid1 + '\'' +
                ", subid2='" + subid2 + '\'' +
                ", subid3='" + subid3 + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public UserSubscription(String subid1, String subid2, String subid3, String uid) {
        this.subid1 = subid1;
        this.subid2 = subid2;
        this.subid3 = subid3;
        this.uid = uid;
    }
}
