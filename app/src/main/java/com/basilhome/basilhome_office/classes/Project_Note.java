package com.basilhome.basilhome_office.classes;

public class Project_Note {
    private String advisor;
    private String note;
    private String time;

    public Project_Note(String advisor, String note, String time) {
        this.advisor = advisor;
        this.note = note;
        this.time = time;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getNote() {
        return note;
    }

    public String getTime() {
        return time;
    }

}
