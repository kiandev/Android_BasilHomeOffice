package com.basilhome.basilhome_office.classes;

public class Notification {
    private String title;
    private String text;
    private String time;

    public Notification(String title, String text, String time) {
        this.title = title;
        this.text = text;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

}
