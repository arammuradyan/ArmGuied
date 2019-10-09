package com.ArmGuide.tourapplication.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceKEY {

    private  List<String> placesList;
    private static PlaceKEY placeKEY;

    public static PlaceKEY getInstance() {
        if (placeKEY == null)
            placeKEY = new PlaceKEY();
        return placeKEY;
    }

    public List<String> getKeyList(){
        return placesList;
    }

    public String getKey(PlaceKey_ByName name){
        return name.getName();
    }

    private PlaceKEY() {
        placesList = new ArrayList<>();
        placesList.add("Aragats");
        placesList.add("Ashtarak");
        placesList.add("Carahunge");
        placesList.add("Dilijan");
        placesList.add("Garni");
        placesList.add("Jermuk");
        placesList.add("KhorVirap");
        placesList.add("Sevan");
        placesList.add("Shikahogh");
        placesList.add("Tatev");
        placesList.add("Tsaghkadzor");
        placesList.add("WaterfallUmbrella");
    }

    private enum PlaceKey_ByName {
        ARAGATS ("Aragats"),
        ASHTARAK ("Ashtarak"),
        CARAHUNGE ("Carahunge"),
        DILIJAN ("Dilijan"),
        GARNI ("Garni"),
        JERMUK ("Jermuk"),
        KHORVIRAP ("KhorVirap"),
        SEVAN ("Sevan"),
        SHIKAHOGH ("Shikahogh"),
        TATEV ("Tatev"),
        TSAGHKADZOR ("Tsaghkadzor"),
        WATERFALLUMBRELLA ("WaterfallUmbrella");

        private final String name;
        private String getName() {
            return name;
        }
        PlaceKey_ByName(String name){
            this.name = name;
        }
    }

}


