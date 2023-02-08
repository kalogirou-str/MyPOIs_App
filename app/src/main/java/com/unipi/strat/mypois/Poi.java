package com.unipi.strat.mypois;

import java.sql.Timestamp;

public class Poi {
    private String title;
    private Timestamp timestamp;
    private double latitude;
    private double longitude;
    private String category;
    private String description;

    public Poi(String title, Timestamp timestamp, double latitude, double longitude, String category, String description) {
        this.title = title;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

