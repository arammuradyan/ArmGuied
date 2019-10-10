package com.ArmGuide.tourapplication.models;

import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable {
    private String name;
    private List<String> imageUrls;
    private String description;
    private double coord_X;
    private double coord_Y;
    private String url_Wiki;

    public Place(){}

    public Place(String name, List<String> imageUrls, String description, double coord_X, double coord_Y, String url_Wiki) {
        this.name = name;
        this.imageUrls = imageUrls;
        this.description = description;
        this.coord_X = coord_X;
        this.coord_Y = coord_Y;
        this.url_Wiki = url_Wiki;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCoord_X() {
        return coord_X;
    }

    public void setCoord_X(double coord_X) {
        this.coord_X = coord_X;
    }

    public double getCoord_Y() {
        return coord_Y;
    }

    public void setCoord_Y(double coord_Y) {
        this.coord_Y = coord_Y;
    }

    public String getUrl_Wiki() {
        return url_Wiki;
    }

    public void setUrl_Wiki(String url_Wiki) {
        this.url_Wiki = url_Wiki;
    }
}
