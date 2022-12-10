package com.example.weatherapp;

public class WeatherWeek {
    private String thoiGian;
    private String resourceAnh;
    private String moTa;
    private String NhietDoMax;
    private String NhietDoMin;
    private String LuongMua;
    private String DoAm;
    private String sunRise;
    private String sunSet;
    private String may;
    private String tocgio;
    private String huonggio;
    private String uv;

    public WeatherWeek(String thoiGian, String resourceAnh, String moTa, String nhietDoMax, String nhietDoMin, String luongMua, String doAm, String sunRise, String sunSet, String may, String tocgio, String huonggio, String uv) {
        this.thoiGian = thoiGian;
        this.resourceAnh = resourceAnh;
        this.moTa = moTa;
        NhietDoMax = nhietDoMax;
        NhietDoMin = nhietDoMin;
        LuongMua = luongMua;
        DoAm = doAm;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.may = may;
        this.tocgio = tocgio;
        this.huonggio = huonggio;
        this.uv = uv;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getResourceAnh() {
        return resourceAnh;
    }

    public void setResourceAnh(String resourceAnh) {
        this.resourceAnh = resourceAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNhietDoMax() {
        return NhietDoMax;
    }

    public void setNhietDoMax(String nhietDoMax) {
        NhietDoMax = nhietDoMax;
    }

    public String getNhietDoMin() {
        return NhietDoMin;
    }

    public void setNhietDoMin(String nhietDoMin) {
        NhietDoMin = nhietDoMin;
    }

    public String getLuongMua() {
        return LuongMua;
    }

    public void setLuongMua(String luongMua) {
        LuongMua = luongMua;
    }

    public String getDoAm() {
        return DoAm;
    }

    public void setDoAm(String doAm) {
        DoAm = doAm;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    public String getTocgio() {
        return tocgio;
    }

    public void setTocgio(String tocgio) {
        this.tocgio = tocgio;
    }

    public String getHuonggio() {
        return huonggio;
    }

    public void setHuonggio(String huonggio) {
        this.huonggio = huonggio;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

}
