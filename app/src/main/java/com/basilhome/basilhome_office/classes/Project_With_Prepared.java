package com.basilhome.basilhome_office.classes;

public class Project_With_Prepared {
    private String project_id;
    private String name;
    private String region;
    private int prepared;

    public Project_With_Prepared(String project_id, String name, String region, int prepared) {
        this.project_id = project_id;
        this.name = name;
        this.region = region;
        this.prepared = prepared;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public int getPrepared() {
        return prepared;
    }
}
