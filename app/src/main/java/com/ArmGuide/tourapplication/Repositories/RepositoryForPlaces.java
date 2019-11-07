package com.ArmGuide.tourapplication.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.models.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RepositoryForPlaces {

    private List<Place> placeList;
    private MutableLiveData<List<Place>> listMutableLiveData;

    public MutableLiveData<List<Place>> getListMutableLiveData() {
        getPlacesListFromFirebase();
        return listMutableLiveData;
    }

    private static RepositoryForPlaces repositoryForPlaces;

    public static RepositoryForPlaces getInstance() {
        if (repositoryForPlaces == null)
            repositoryForPlaces = new RepositoryForPlaces();
        return repositoryForPlaces;
    }

    private RepositoryForPlaces() {
        listMutableLiveData = new MutableLiveData<>();
        placeList = new ArrayList<>();
    }

     private void getPlacesListFromFirebase() {
         FirebaseDatabase.getInstance().getReference().child("Places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                placeList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()
                ) {
                    placeList.add(d.getValue(Place.class));
                }
                Log.d("MyLog","placeList "+placeList.get(11).getName());
                listMutableLiveData.setValue(placeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
