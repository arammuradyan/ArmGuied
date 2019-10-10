package com.ArmGuide.tourapplication.models;

public enum PlacesNames {
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

    public String getName() {
        return name;
    }
    PlacesNames(String name){
        this.name = name;
    }
}
