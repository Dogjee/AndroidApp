package com.example.weatherapp;

public class weather {
    private String thoigian;
    private String resourceId;
    private String nhietDo;


    public weather(String thoigian, String resourceId, String nhietDo) {
        this.thoigian = thoigian;
        this.resourceId = resourceId;
        this.nhietDo = nhietDo;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getNhietDo() {
        return nhietDo;
    }

    public void setNhietDo(String nhietDo) {
        this.nhietDo = nhietDo;
    }
}
