package com.basilhome.basilhome_office.classes;

public class Project {
    private String project_id;
    private String name;
    private String region;

    public Project(String project_id, String name, String region) {
        this.project_id = project_id;
        this.name = name;
        this.region = region;
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
}
