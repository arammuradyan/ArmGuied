package com.ArmGuide.tourapplication.ui.map;

import android.net.Uri;
public class PlaceInfo {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private Uri websiteUri;
    private com.google.android.gms.maps.model.LatLng latLng;
    private Double reiting;
    private Integer userReitingsTotal;

    public PlaceInfo() {
    }

    public PlaceInfo(String id, String name, String address, String phoneNumber,
                     Uri websiteUri, com.google.android.gms.maps.model.LatLng latLng,
                     Double reiting, Integer userReitingsTotal) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.reiting = reiting;
        this.userReitingsTotal = userReitingsTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public com.google.android.gms.maps.model.LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        this.latLng = latLng;
    }

    public Double getReiting() {
        return reiting;
    }

    public void setReiting(Double reiting) {
        this.reiting = reiting;
    }

    public Integer getUserReitingsTotal() {
        return userReitingsTotal;
    }

    public void setUserReitingsTotal(Integer userReitingsTotal) {
        this.userReitingsTotal = userReitingsTotal;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", reiting=" + reiting +
                ", userReitingsTotal=" + userReitingsTotal +
                '}';
    }
}
