package com.ArmGuide.tourapplication.ui.map;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class  PlaceInfoRepository {
    // PLACES constants
    public static final String DILIJAN="dilijan";
    public static final String CAXKADZOR="caxkadzor";

    // ZOOM constants
    public static final float ZOOM_WORLD=1.0f;
    public static final float ZOOM_CONTINENT=5.0f;
    public static final float ZOOM_CITY=10.0f;
    public static final float ZOOM_STREETS=15.0f;
    public static final float ZOOM_BUILDINGS=20.0f;

    public static PlaceInfo getPlaceInfo(String placeName){

        if(placeName.equals(DILIJAN)){
            com.google.android.gms.maps.model.LatLng dilijan= new LatLng(40.7405524,
                                                                         44.8625965);
            PlaceInfo placeInfo=new PlaceInfo();
            placeInfo.setLatLng(dilijan);
            placeInfo.setName(DILIJAN);
            placeInfo.setWebsiteUri(Uri.parse("https://www.youtube.com/watch?v=iGbMNfv2KxA"));
            placeInfo.setPhoneNumber("+374 544 644");
            return placeInfo;
        }
        if(placeName.equals(CAXKADZOR)){
            com.google.android.gms.maps.model.LatLng dilijan= new LatLng(40.5317434,
                                                                         44.7159759);
            PlaceInfo placeInfo=new PlaceInfo();
            placeInfo.setLatLng(dilijan);
            placeInfo.setName(CAXKADZOR);
            placeInfo.setWebsiteUri(Uri.parse("https://www.youtube.com/watch?v=iGbMNfv2KxA"));
            placeInfo.setPhoneNumber("094 894 644");
            return placeInfo;
        }
        else {
            return null;
        }
    }

}
