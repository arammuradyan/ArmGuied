package com.ArmGuide.tourapplication.models;

import java.util.List;

public class Company {
    private String id;
    private String email;
    private String companyName;
    private String password;
    private String phoneNumber;
    private String address;
    private String avatarUrl;
    private String webUrl;
    private boolean IsCompany;
    private List<Tour> tours;

    public Company() {
    }

    public Company(String id, String email, String companyName, String password,
                   String phoneNumber, String address, String avatarUrl,
                   String webUrl, boolean isCompany, List<Tour> tours) {
        this.id = id;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.webUrl = webUrl;
        IsCompany = isCompany;
        this.tours = tours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public boolean isCompany() {
        return IsCompany;
    }

    public void setCompany(boolean company) {
        IsCompany = company;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }
}
