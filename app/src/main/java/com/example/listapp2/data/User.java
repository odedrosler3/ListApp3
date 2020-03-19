package com.example.listapp2.data;

import java.util.List;

public class User {
    private String phonenumer;
    private String name;
    private List<Group> groups;



    public User(String phonenumer, String name, List<Group> groups) {
        this.phonenumer = phonenumer;
        this.name = name;
        this.groups = groups;
    }

    public String getPhonenumer() {
        return phonenumer;
    }

    public void setPhonenumer(String phonenumer) {
        this.phonenumer = phonenumer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
