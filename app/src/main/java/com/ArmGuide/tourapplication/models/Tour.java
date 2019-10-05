package com.ArmGuide.tourapplication.models;

public class Tour {
    private String id, placeName;
    private int price;

    public Tour() {
    }

    public Tour(String id, String placeName, int price) {
        this.id = id;
        this.placeName = placeName;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id='" + id + '\'' +
                ", placeName='" + placeName + '\'' +
                ", price=" + price +
                '}';
    }
}
