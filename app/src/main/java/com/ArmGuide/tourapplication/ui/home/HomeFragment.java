package com.ArmGuide.tourapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ArmGuide.tourapplication.R;

public class HomeFragment extends Fragment {


    ViewPager viewPagerLand;
    AdapterViewPager adapterViewPager;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewPagerLand = view.findViewById(R.id.viewPagerLand);
        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager());
        viewPagerLand.setAdapter(adapterViewPager);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //viewPagerLand.setAdapter(adapterViewPager);

    }
}