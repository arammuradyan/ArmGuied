package com.ArmGuide.tourapplication.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecialFilteringClass {
    private List<Filter> filters;
    private List<Tour> tours;
    private List<Tour> toursUnderCondition;
    private List<Tour> toursAlreadySeen;
    private List<Tour> finalTours;

    public SpecialFilteringClass() {
        toursUnderCondition = new ArrayList<>();
        toursAlreadySeen = new ArrayList<>();
        finalTours = new ArrayList<>();
        filters = new ArrayList<>();
        tours = new ArrayList<>();
    }

    public void setCriteria(List<Filter> filters) {
        if(filters!=null && filters.size()>0) {
            this.filters.clear();
            this.filters.addAll(filters);
            Log.d("polo", "inside setCriteria, filters: " + filters.size());
        }

    }

    public void setTours(List<Tour> tours) {
        if(tours!=null && tours.size()>0) {
            this.tours.clear();
            this.tours.addAll(tours);
            Log.d("polo", "inside setTours, tours: " + tours.size());
        }
    }

    public List<Tour> getToursUnderCondition() {
        toursUnderCondition.clear();
        if (filters != null && tours != null && filters.size() > 0 && tours.size() > 0) {

            for (Tour tour : tours
            ) {
                String tourPlaceName = tour.getPlaceName();
                String tourStartDate = tour.getDate();
                int tourPrice = tour.getPrice();
                boolean tourTransport = tour.isTransport();
                boolean tourGuide = tour.isThreeLangGuide();
                boolean tourWine = tour.isVineDegustation();
                boolean tourWifi = tour.isWifi();
                boolean tourFood = tour.isFood();

                for (Filter f : filters
                ) {
                    String placeName = f.getPlaceName();
                    String dateFrom = f.getDateFrom();
                    String dateTo = f.getDateTo();
                    int priceFrom = f.getPriceFrom();
                    int priceTo = f.getPriceTo();
                    boolean transportMust = f.isTransportMust();
                    boolean foodMust = f.isFoodMust();
                    boolean guideMust = f.isGuideMust();
                    boolean wineMust = f.isWineMust();
                    boolean wifiMust = f.isWifiMust();

                    //checking
                    boolean isNamesEqual = placeName.equals(tourPlaceName);

                    //stugum enq jamketi skizby
                    boolean isDataFromOk;
                    {
                        if (dateFrom != null) {
                            if (compareDates(dateFrom, tourStartDate) <= 0)
                                isDataFromOk = true;
                            else
                                isDataFromOk = false;
                        } else
                            isDataFromOk = true;
                    }

                    //stugum enq jamketi verjy
                    boolean isDataToOk;
                    {
                        if (dateTo != null) {
                            if (compareDates(dateTo, tourStartDate) >= 0)
                                isDataToOk = true;
                            else
                                isDataToOk = false;
                        } else
                            isDataToOk = true;
                    }

                    boolean isDataCombineOk = isDataFromOk && isDataToOk;
                    boolean isPriceOk = priceFrom <= tourPrice && tourPrice <= priceTo;
                    boolean isWineOk = tourWine || !wineMust;
                    boolean isGuideOk = tourGuide || !guideMust;
                    boolean isTransportOk = tourTransport || !transportMust;
                    boolean isFoodOk = tourFood || !foodMust;
                    boolean isWifiOk = tourWifi || !wifiMust;

                    if (isNamesEqual && isDataCombineOk && isPriceOk && isWifiOk && isWineOk
                            && isFoodOk && isGuideOk && isTransportOk) {
                        toursUnderCondition.add(tour);
                        break;
                    }
                }
            }
        }
        return toursUnderCondition;
    }

    private int compareDates(String from, String to) {
        int result = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom = formatter.parse(from);
            Date dateTo = formatter.parse(to);
            result = dateFrom.compareTo(dateTo);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public List<Tour> setToursToFilter(List<Tour> alreadySeen, List<Tour> newTours) {
        finalTours = newTours;

        Log.d("polo", "inside setToursToFilter, alreadySeen: " + alreadySeen.size());
        Log.d("polo", "inside setToursToFilter, newTours: " + newTours.size());
        if (newTours.size() > 0 && alreadySeen.size() > 0) {
            for (Tour tAlSeen : alreadySeen
            ) {
                String keyTAlSeen = tAlSeen.getId();
                for (Tour newTour : newTours
                ) {
                    if (keyTAlSeen.equals(newTour.getId())) {
                        finalTours.remove(newTour);
                        break;
                    }
                }
            }
        }
        Log.d("polo", "inside setToursToFilter, finalTours: " + finalTours.size());
        return finalTours;
    }


}
