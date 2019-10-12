package com.ArmGuide.tourapplication.ui.home;

import android.util.Log;

import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class SpecialClass {
    private List<String> placeNames;
    private Tour tour;

    public void setPlaceNames(List<String> placeNames) {
        if (this.placeNames != null) {
            this.placeNames.clear();
            this.placeNames.addAll(placeNames);
        }
        this.placeNames = placeNames;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tour getTourUnderCondition() {
        Tour tourUnderCondition = null;
        if (tour != null && placeNames.size() > 0) {
            for (String placeName : placeNames
            ) {
                if (placeName.equals(tour.getPlaceName())) {
                    tourUnderCondition = tour;
                    break;
                }
            }
        }
        Log.d("dobbi", "from special class result/" + tourUnderCondition);
        if (tour != null)
            Log.d("dobbi", "from specia class tourInput/" + tour.getPlaceName());
        Log.d("dobbi", "from special class placesNames/" + placeNames);

        return tourUnderCondition;
    }
}
