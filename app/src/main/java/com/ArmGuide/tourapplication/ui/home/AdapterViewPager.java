package com.ArmGuide.tourapplication.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlacesNames;

import java.util.List;


public class AdapterViewPager extends FragmentPagerAdapter {

    private List<Place> places;
    public AdapterViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    public AdapterViewPager(@NonNull FragmentManager fm, List<Place> places){
        super(fm);
        this.places = places;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        BlankFragment landFragment  = new BlankFragment(places.get(position));


        /*switch (position) {
            case 0:
                landFragment = new BlankFragment(PlacesNames.CARAHUNGE);
                break;
            case 1:
                landFragment = new BlankFragment(PlacesNames.GARNI);
                break;
            case 2:
                landFragment = new BlankFragment(PlacesNames.ASHTARAK);
                break;
            case 3:
                landFragment = new BlankFragment(PlacesNames.SEVAN);
                break;
            case 4:
                landFragment = new BlankFragment(PlacesNames.DILIJAN);
                break;
            case 5:
                landFragment = new BlankFragment(PlacesNames.ARAGATS);
                break;
            case 6:
                landFragment = new BlankFragment(PlacesNames.TATEV);
                break;
            case 7:
                landFragment = new BlankFragment(PlacesNames.SHIKAHOGH);
                break;
            case 8:
                landFragment = new BlankFragment(PlacesNames.JERMUK);
                break;
            case 9:
                landFragment = new BlankFragment(PlacesNames.KHORVIRAP);
                break;
            case 10:
                landFragment = new BlankFragment(PlacesNames.WATERFALLUMBRELLA);
                break;
            case 11:
                landFragment = new BlankFragment(PlacesNames.TSAGHKADZOR);
                break;
            default:
                break;
        }*/
        return landFragment;
    }

    @Override
    public int getCount() {
        return places.size();
    }
}
