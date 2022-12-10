package com.example.weatherapp;

public class History_hour {
    private String icon;
    private String date;
    private String temp;
    private String wind;
    private String humidity;
    private String description;

    public History_hour(String icon, String date, String temp, String wind, String humidity, String description) {
        this.icon = icon;
        this.date = date;
        this.temp = temp;
        this.wind = wind;
        this.humidity = humidity;
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
