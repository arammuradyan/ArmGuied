package com.ArmGuide.tourapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tourist;
import com.ArmGuide.tourapplication.models.UserState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Place> places = new ArrayList<>();
    HomeViewModel homeViewModel;
    ViewPager viewPagerLand;
    AdapterViewPager adapterViewPager;
    UserState userState = UserState.NO_REGISTRATED;
    private String uId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserState();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(HomeFragment.this).get(HomeViewModel.class);

        Log.d("MyLog", "HomeFragment - onCreateView");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Log.d("MyLog", "HomeFragment - onViewCreated");

        viewPagerLand = view.findViewById(R.id.viewPagerLand);
        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager(), places, userState);
        viewPagerLand.setAdapter(adapterViewPager);
        test();

    }


    private void test() {
        FirebaseDatabase.getInstance().getReference().child("Places")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot d : dataSnapshot.getChildren()
                        ) {
                            Place curPlace = d.getValue(Place.class);
                            places.add(curPlace);
                        }
                        adapterViewPager.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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



    private void getUserState(){

        if((FirebaseAuth.getInstance().getCurrentUser() != null)){
            uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Companies").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()
                    ) {
                        Company company = d.getValue(Company.class);
                        if (company.getId().equals(uId)) {
                            userState = UserState.COMPANY;
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (userState != UserState.NO_REGISTRATED && userState != UserState.COMPANY)
            userState = UserState.TOURIST;
    }

}