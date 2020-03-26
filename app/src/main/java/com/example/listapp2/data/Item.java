package com.example.listapp2.data;

import android.media.Image;

public class Item {
    private int id;
    private String name;
    private String desc;
    private String imagelimk;
    public static int countitem=1;

    public Item(String namestr, String descstr, String imagelnkstr) {
    }

    public Item(int id, String name, String desc, String imagelimk) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imagelimk = imagelimk;
    }

    public Item(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Item(String s, String name, String namestr, String descstr) {
    }

    public Item(String namestr, String descstr) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImagelimk() {
        return imagelimk;
    }

    public void setImagelimk(String imagelimk) {
        this.imagelimk = imagelimk;
    }
}
