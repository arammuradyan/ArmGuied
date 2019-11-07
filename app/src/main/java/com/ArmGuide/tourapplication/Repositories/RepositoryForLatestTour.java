package com.ArmGuide.tourapplication.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.models.Tour;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RepositoryForLatestTour {

    private MutableLiveData<Tour> mutableLiveData;

    private static RepositoryForLatestTour repositoryForLatestTour;

    public static RepositoryForLatestTour getInstance() {
        if (repositoryForLatestTour == null)
            repositoryForLatestTour = new RepositoryForLatestTour();
        return repositoryForLatestTour;
    }

    private RepositoryForLatestTour() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Tour> getLiveData() {
        getLatestTour();
        return mutableLiveData;
    }

    private void getLatestTour() {
        FirebaseDatabase.getInstance().getReference().child("Tours").orderByKey().limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Tour latestTour = new Tour();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                        ) {
                            latestTour = snapshot.getValue(Tour.class);
                        }
                        mutableLiveData.setValue(latestTour);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
