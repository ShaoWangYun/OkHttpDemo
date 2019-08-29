package com.imswy.okhttpdemo.bean;

import java.util.Arrays;


public class User {
    private String note;
    private int id;
    private byte[] icon;
    private String iconUrl;
    private String account;
    private String password;

    public String getNote () {
        return note;
    }

    public void setNote (String note) {
        this.note = note;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public byte[] getIcon () {
        return icon;
    }

    public void setIcon (byte[] icon) {
        this.icon = icon;
    }

    public String getIconUrl () {
        return iconUrl;
    }

    public void setIconUrl (String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccount () {
        return account;
    }

    public void setAccount (String account) {
        this.account = account;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public User (String note, int id, byte[] icon, String iconUrl, String account, String password) {
        this.note = note;
        this.id = id;
        this.icon = icon;
        this.iconUrl = iconUrl;
        this.account = account;
        this.password = password;
    }

    public User () {
        super();
    }

    @Override
    public String toString () {
        return "User{" +
                "note='" + note + '\'' +
                ", id=" + id +
                ", icon=" + Arrays.toString(icon) +
                ", iconUrl='" + iconUrl + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}