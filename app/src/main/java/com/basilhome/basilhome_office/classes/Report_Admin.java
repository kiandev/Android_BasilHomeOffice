package com.basilhome.basilhome_office.classes;

public class Report_Admin {
    private String text;
    private String advisor;
    private String date;
    private static String img;

    public Report_Admin(String text, String advisor, String date, String img) {
        this.text = text;
        this.advisor = advisor;
        this.date = date;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getDate() {
        return date;
    }

    public static String getImg() {
        return img;
    }
}
