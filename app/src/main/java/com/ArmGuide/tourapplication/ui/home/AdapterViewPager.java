package com.ArmGuide.tourapplication.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class AdapterViewPager extends FragmentPagerAdapter {

    public AdapterViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment landFragment = null;
        switch (position) {
            case 0:
                landFragment = new BlankFragment();
                break;
            case 1:
                landFragment = new BlankFragment();
                break;
            case 2:
                landFragment = new BlankFragment();
                break;

        }
        return landFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
