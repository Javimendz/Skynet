package com.example.skynet.ui.main;

public class Achievement {
    private String title;
    private String icon;
    private String date;

    public Achievement(String title, String icon, String date) {
        this.title = title;
        this.icon = icon;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getIcon() { return icon; }
    public String getDate() { return date; }
}