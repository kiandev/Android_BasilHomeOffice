package com.basilhome.basilhome_office.classes;

public class Customer_Note {
    private String customer_id;
    private String note;
    private String advisor;
    private String time;

    public Customer_Note(String customer_id, String note, String advisor, String time) {
        this.customer_id = customer_id;
        this.note = note;
        this.advisor = advisor;
        this.time = time;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public String getNote() {
        return note;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getTime() {
        return time;
    }
}
