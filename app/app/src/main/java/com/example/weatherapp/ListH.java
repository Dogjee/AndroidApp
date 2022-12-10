package com.example.weatherapp;

import java.io.Serializable;

public class ListH implements Serializable {
    private String imgSource;
    private String ngay;

    public ListH(String imgSource, String ngay) {
        this.imgSource = imgSource;
        this.ngay = ngay;
    }

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
