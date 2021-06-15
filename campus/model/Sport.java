package com.tfg.campus.model;

public class Sport {
    private String description;
    private String name;
    private String sid;
    private String weekday;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public Sport(String description, String name, String sid, String weekday) {
        this.description = description;
        this.name = name;
        this.sid = sid;
        this.weekday = weekday;
    }

    public Sport() {
    }

    @Override
    public String toString() {
        return "Sport{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", sid='" + sid + '\'' +
                ", weekday='" + weekday + '\'' +
                '}';
    }
}
