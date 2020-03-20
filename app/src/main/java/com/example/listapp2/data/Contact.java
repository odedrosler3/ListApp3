package com.example.listapp2.data;

public class Contact {
    private String Nickname;
    private String phonenumber;

    public Contact(String nickname, String phonenumber) {
        Nickname = nickname;
        this.phonenumber = phonenumber;
    }

    public Contact() {
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

}
