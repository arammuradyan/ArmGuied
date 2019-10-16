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

    public SpecialFilteringClass() {
        toursUnderCondition = new ArrayList<>();
    }

    public void setPlaceNames(List<Filter> filters) {
        if (this.filters != null) {
            this.filters.clear();
            this.filters.addAll(filters);
        }
        this.filters = filters;
    }

    public void setTours(List<Tour> tours) {
        if (this.tours != null) {
            this.tours.clear();
            this.tours.addAll(tours);
        }
        this.tours = tours;
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
}
