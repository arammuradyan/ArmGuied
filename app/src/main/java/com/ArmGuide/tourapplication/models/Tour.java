package com.ArmGuide.tourapplication.models;

import java.util.List;

public class Tour {
    private String id,placeName, imgUrl, date, moreInfo;
    private Company tourCompany;
    private int price;
    private boolean transport, food, threeLangGuide, vineDegustation, wifi;
    private List<String> touristsIds;

    public Tour() {
    }

    public Tour(String id, String placeName, String imgUrl, String date, String moreInfo, Company tourCompany, int price,
                boolean transport, boolean food, boolean threeLangGuide, boolean vineDegustation, boolean wifi,
                List<String> touristsIds) {
        this.id = id;
        this.placeName = placeName;
        this.imgUrl = imgUrl;
        this.date = date;
        this.moreInfo = moreInfo;
        this.tourCompany = tourCompany;
        this.price = price;
        this.transport = transport;
        this.food = food;
        this.threeLangGuide = threeLangGuide;
        this.vineDegustation = vineDegustation;
        this.wifi = wifi;
        this.touristsIds = touristsIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public Company getTourCompany() {
        return tourCompany;
    }

    public void setTourCompany(Company tourCompany) {
        this.tourCompany = tourCompany;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isTransport() {
        return transport;
    }

    public void setTransport(boolean transport) {
        this.transport = transport;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isThreeLangGuide() {
        return threeLangGuide;
    }

    public void setThreeLangGuide(boolean threeLangGuide) {
        this.threeLangGuide = threeLangGuide;
    }

    public boolean isVineDegustation() {
        return vineDegustation;
    }

    public void setVineDegustation(boolean vineDegustation) {
        this.vineDegustation = vineDegustation;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public List<String> getTouristsIds() {
        return touristsIds;
    }

    public void setTouristsIds(List<String> touristsIds) {
        this.touristsIds = touristsIds;
    }
}
