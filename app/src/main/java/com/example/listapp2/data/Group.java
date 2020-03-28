package com.example.listapp2.data;

import java.util.List;

public class Group {
    private Long Id;
    public static int lastId;
    private String Name;
    private List<Contact> contacts;
    private List<Item> items;


    public Group(Long id, String name, List<Contact> contacts, List<Item> items) {
        Id = id;
        Name = name;
        this.contacts = contacts;
        this.items = items;
    }

    public Group(Long id, String name, List<Contact> contacts) {
        Id = id;
        Name = name;
        this.contacts = contacts;
    }

    public Group() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        Group.lastId = lastId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    //todo  פעולה ששולפת את המידע מאנשי הקשר
}
