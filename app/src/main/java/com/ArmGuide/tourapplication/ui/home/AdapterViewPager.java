package com.ArmGuide.tourapplication.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlaceKEY;
import com.ArmGuide.tourapplication.models.UserState;

import java.util.List;


public class AdapterViewPager extends FragmentPagerAdapter {

    private List<Place> places;
    private List<String> placeKeys;
    private UserState state;

    public AdapterViewPager(@NonNull FragmentManager fm, List<Place> places, UserState state){
        super(fm);
            this.places = places;
            placeKeys = PlaceKEY.getInstance().getKeyList();
            this.state = state;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        String key = placeKeys.get(position);
        BlankFragment landFragment  = new BlankFragment(places.get(position),key, state);
        return landFragment;
    }

    @Override
    public int getCount() {
        return places!=null?places.size():0;
    }
}
