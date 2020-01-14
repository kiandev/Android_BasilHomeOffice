package com.basilhome.basilhome_office.classes;

public class Customer_Full {
    private String customer_id;
    private String name;
    private String grade;
    private String advisor;
    private String create;
    private String readed;
    private String tel;
    private int read;


    public Customer_Full(String customer_id, String name, String grade, String advisor, String create, String readed, String tel, int read) {
        this.customer_id = customer_id;
        this.name = name;
        this.grade = grade;
        this.advisor = advisor;
        this.create = create;
        this.readed = readed;
        this.tel = tel;
        this.read = read;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getCreate() {
        return create;
    }

    public String getReaded() {
        return readed;
    }

    public String getTel() {
        return tel;
    }

    public int getRead() {
        return read;
    }
}
