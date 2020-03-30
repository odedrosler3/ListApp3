package com.example.listapp2.data;

import java.util.List;

public class Group {
    private Long id;
    public static int lastId;
    private String name;
    private List<Contact> contacts;
    private List<Item> items;
    private String admin;

    public String getAdmin() {
        return admin;
    }



    public Group(Group g) {
        this.id = g.getid();
        this.name = g.getname();
        this.contacts = g.getContacts();
        this.items = g.getItems();
        this.admin = g.getAdmin();
    }

    public Group(Long id, String name, List<Contact> contacts, List<Item> items, String admin) {
        this.id = id;
        this.name = name;
        this.contacts = contacts;
        this.items = items;
        this.admin = admin;
    }

    public Group(Long id, String name, List<Contact> contacts, String admin) {
        this.id = id;
        this.name = name;
        this.contacts = contacts;
        this.admin = admin;
    }

    public Group(Long id, String name, String admin) {
        this.id = id;
        this.name = name;
        this.admin = admin;
    }
    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group() {
    }

    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        Group.lastId = lastId;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
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
