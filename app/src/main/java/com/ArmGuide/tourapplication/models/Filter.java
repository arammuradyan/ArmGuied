package com.ArmGuide.tourapplication.models;

import java.io.Serializable;

public class Filter implements Serializable {
    private String placeName;
    private String dateFrom;
    private String dateTo;
    private int priceFrom;
    private int priceTo;
    private boolean transportMust;
    private boolean foodMust;
    private boolean guideMust;
    private boolean wineMust;
    private boolean wifiMust;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public boolean isTransportMust() {
        return transportMust;
    }

    public void setTransportMust(boolean transportMust) {
        this.transportMust = transportMust;
    }

    public boolean isFoodMust() {
        return foodMust;
    }

    public void setFoodMust(boolean foodMust) {
        this.foodMust = foodMust;
    }

    public boolean isGuideMust() {
        return guideMust;
    }

    public void setGuideMust(boolean guideMust) {
        this.guideMust = guideMust;
    }

    public boolean isWineMust() {
        return wineMust;
    }

    public void setWineMust(boolean wineMust) {
        this.wineMust = wineMust;
    }

    public boolean isWifiMust() {
        return wifiMust;
    }

    public void setWifiMust(boolean wifiMust) {
        this.wifiMust = wifiMust;
    }
}
