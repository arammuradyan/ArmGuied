package com.ArmGuide.tourapplication.ui.home;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlaceKEY;
import com.ArmGuide.tourapplication.models.UserState;

import java.util.ArrayList;
import java.util.List;


public class AdapterViewPager extends FragmentPagerAdapter {

    private List<Place> places;
    private List<BlankFragment> blackFragments;
    private List<String> placeKeys;
    private UserState state;

    public AdapterViewPager(@NonNull FragmentManager fm, UserState state) {
        super(fm);
        this.places = new ArrayList<>();
        blackFragments = new ArrayList<>();
        placeKeys = PlaceKEY.getInstance().getKeyList();
        this.state = state;
        Log.d("MyLog",  " Adapter constructor");

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("MyLog", position + " Adapter place " + places.get(position).getName());
        return super.instantiateItem(container, position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String key = placeKeys.get(position);
        Log.d("MyLog", position + " Adapter place " + places.get(position).getName());

        return blackFragments.get(position);
    }

    public BlankFragment getFragment(int positon) {
        return blackFragments.get(positon);
    }

    @Override
    public int getCount() {
        return blackFragments.size();
//        return places != null ? places.size() : 0;
    }

    public void setPlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        init();
    }

    public void setState(UserState state) {
        this.state = state;
        init();
    }

    private void init() {
        if (state == null)
            return;
        blackFragments.clear();

        for (int i = 0; i < places.size(); i++) {
            BlankFragment blankFragment = new BlankFragment(places.get(i), placeKeys.get(i), state);
            blackFragments.add(blankFragment);
        }
        notifyDataSetChanged();
    }
}
