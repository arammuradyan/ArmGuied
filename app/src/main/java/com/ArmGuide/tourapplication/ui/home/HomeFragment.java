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
import com.ArmGuide.tourapplication.models.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    ViewPager viewPagerLand;
    AdapterViewPager adapterViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPagerLand = view.findViewById(R.id.viewPagerLand);
        test();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void test(){
        FirebaseDatabase.getInstance().getReference().child("Places")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    List<Place> places = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot d: dataSnapshot.getChildren()
                        ) {
                            Place curPlace = d.getValue(Place.class);
                            places.add(curPlace);
                        }
                        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager(),places);
                        viewPagerLand.setAdapter(adapterViewPager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}