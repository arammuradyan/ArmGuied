package com.ArmGuide.tourapplication.ui.home;

import android.content.SharedPreferences;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tourist;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.Images.AdapetZoomedImages;
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
    UserState userState;
    private String uId;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerViewBlank;
    private AdapterRecyclerBlank adapterRecyclerBlank;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ViewModelProviders.of(HomeFragment.this).get(HomeViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("statePref", 0);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d("MyLog", "HomeFragment - onCreateView");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserState();
        Log.d("MyLog", "HomeFragment - onViewCreated");
        viewPagerLand = view.findViewById(R.id.viewPagerLand);
        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager(), userState);
        viewPagerLand.setAdapter(adapterViewPager);

       /* recyclerViewBlank = view.findViewById(R.id.recycler_view_blank);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewBlank.setHasFixedSize(true);
        recyclerViewBlank.setLayoutManager(linearLayoutManager);
        adapterRecyclerBlank = new AdapterRecyclerBlank(places,userState);
        recyclerViewBlank.setAdapter(adapterRecyclerBlank);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewBlank);*/


        homeViewModel.getLiveData().observe(HomeFragment.this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> data) {
                if (places == null) {
                    return;
                }
//                Log.d("MyLog", "Fragmwnt placeList " + data.get(11).getName());
                places = data;
                adapterViewPager.setPlaces(places);

                // adapterRecyclerBlank.setPlaces(data);
            }
        });


//        test();
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
        getUserState();
        /*
        Log.d("MyLog", "HomeFragment - onResume");
        String newState = sharedPreferences.getString("newState", "def");
        Log.d("MyLog", newState);
        if (newState.equals("newState")) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(HomeFragment.this)
                    .attach(HomeFragment.this)
                    .commit();
            sharedPreferences.edit().clear().apply();
            Log.d("MyLog", "shared pref recreated fragment and cleared.");
        }*/
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


    private void getUserState() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            userState = UserState.NO_REGISTRATED;
        else {
            uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Companies").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()
                    ) {
                        Company company = d.getValue(Company.class);
                        if (company.getId().equals(uId)) {
                            userState = UserState.COMPANY;
                            adapterViewPager.setState(userState);
                            // adapterRecyclerBlank.setUserState(userState);
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