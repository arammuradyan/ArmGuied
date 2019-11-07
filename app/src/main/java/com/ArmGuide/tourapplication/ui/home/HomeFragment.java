package com.ArmGuide.tourapplication.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.ServiceForFilteredNotifications;
import com.ArmGuide.tourapplication.models.UserState;

import java.util.List;

import su.levenetc.android.textsurface.TextSurface;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AdapterViewPager adapterViewPager;
    private TextSurface textSurface;


    private ArrayList<Place> placies=new ArrayList<>();


    public HomeFragment(ArrayList<Place> placies) {
        this.placies = placies;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("MyLog", "HomeFragment - onCreate");
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);



    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("MyLog", "HomeFragment - onCreateView");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textSurface = view.findViewById(R.id.text_surface);



                    textSurface.post(new Runnable() {
                        @Override
                        public void run() {
                            show();
                        }
                    });




        Log.d("MyLog", "HomeFragment - onViewCreated");
        ViewPager viewPagerLand = view.findViewById(R.id.viewPagerLand);
        if (getActivity() != null)
            adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager());
        viewPagerLand.setAdapter(adapterViewPager);

        adapterViewPager.setPlaces(placies);


        /*homeViewModel.getLiveData().observe(HomeFragment.this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> data) {
                if (data != null){
                    adapterViewPager.setPlaces(data);


                  }

            }
        });*/

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

    private void show() {
        textSurface.reset();
        TextViewAnimation.play(textSurface, getActivity().getAssets());
    }
}
