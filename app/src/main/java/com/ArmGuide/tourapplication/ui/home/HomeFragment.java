package com.ArmGuide.tourapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Place;

import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AdapterViewPager adapterViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MyLog", "HomeFragment - onCreate");
        homeViewModel = ViewModelProviders.of(HomeFragment.this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("MyLog", "HomeFragment - onCreateView");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("MyLog", "HomeFragment - onViewCreated");
        ViewPager viewPagerLand = view.findViewById(R.id.viewPagerLand);
        if (getActivity() != null)
            adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager());
        viewPagerLand.setAdapter(adapterViewPager);

        homeViewModel.getLiveData().observe(HomeFragment.this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> data) {
                if (data != null)
                    adapterViewPager.setPlaces(data);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MyLog", "HomeFragment - onResume");
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d("MyLog", "HomeFragment - onStart");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MyLog", "HomeFragment - onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("MyLog", "HomeFragment - onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyLog", "HomeFragment - onDestroy");
    }
}
