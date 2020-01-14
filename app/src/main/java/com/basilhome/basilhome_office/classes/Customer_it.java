package com.basilhome.basilhome_office.classes;

public class Customer_it {
    private String customer_id;
    private String name;
    private String tel;
    private String advisor;
    private int read;

    public Customer_it(String customer_id, String name, String tel, String advisor, int read) {
        this.customer_id = customer_id;
        this.name = name;
        this.tel = tel;
        this.advisor = advisor;
        this.read = read;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAdvisor() {
        return advisor;
    }

    public int getRead() {
        return read;
    }
}
