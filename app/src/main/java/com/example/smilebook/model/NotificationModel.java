package com.example.smilebook.model;

public class NotificationModel {
    private String alarm_text;

    public NotificationModel(String alarm_text) {
        this.alarm_text = alarm_text;
    }

    public String getAlarm_text() {
        return alarm_text;
    }

    public void setAlarm_text(String alarm_text) {
        this.alarm_text = alarm_text;
    }
}
