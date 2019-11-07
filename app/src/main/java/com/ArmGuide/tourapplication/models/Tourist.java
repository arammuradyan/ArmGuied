package com.ArmGuide.tourapplication.models;

import java.util.List;

public class Tourist {
   private String id;
   private String email;
   private String fullName;
   private String password;
   private String phoneNumber;
   private String avatarUrl;
   private boolean IsCompany;
   private List<Tour> tours;
   private List<String> subscribedPlacesIds;
   private String question;
   private String answer;
   private List<Filter> filters;
   private List<Tour> toursAlreadySeen;
   private List<Tour> notifications;

    public Tourist() {
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Tour> getToursAlreadySeen() {
        return toursAlreadySeen;
    }

    public void setToursAlreadySeen(List<Tour> toursAlreadySeen) {
        this.toursAlreadySeen = toursAlreadySeen;
    }

   /* public Tourist(String id, String email, String fullName,
                   String password, String phoneNumber, String avatarUrl,
                   boolean isCompany, List<Tour> tours) {*/

    public Tourist(String id, String email, String fullName, String password,
                   String phoneNumber, String avatarUrl, boolean isCompany,
                   List<Tour> tours, List<String> subscribedPlacesIds, String question, String answer) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        IsCompany = isCompany;
        this.tours = tours;
        this.subscribedPlacesIds = subscribedPlacesIds;
        this.question = question;
        this.answer = answer;
    }

    public boolean isCompany() {
        return IsCompany;
    }

    public void setCompany(boolean company) {
        IsCompany = company;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean getIsCompany() {
        return IsCompany;
    }

    public void setIsCompany(boolean company) {
        IsCompany = company;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public List<String> getSubscribedPlacesIds() {
        return subscribedPlacesIds;
    }

    public void setSubscribedPlacesIds(List<String> subscribedPlacesIds) {
        this.subscribedPlacesIds = subscribedPlacesIds;
    }

    @Override
    public String toString() {
        return "Tourist{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", IsCompany=" + IsCompany +
                ", tours=" + tours +
                '}';
    }
}
