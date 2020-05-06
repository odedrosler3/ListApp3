package com.example.listapp2.data;

import static java.lang.Long.getLong;

public class Item {
    private long id;
    private String name;
    private String desc;
    private String imagelink;




    public Item(String name, String desc,Long id) {
        this.id = id;
        this.name= name;
        this.desc=desc;

    }


    public Item(String name, String desc,long id, String imagelink) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imagelink = imagelink;
    }

    public Item(String s, String name, String namestr, String descstr) {
    }

    public Item() {
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}
