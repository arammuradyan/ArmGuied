package com.ArmGuide.tourapplication.Repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlacesNames;
import com.google.android.gms.gcm.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;


public class RepositoryForPlaces {

    private Place place;
    private PlacesNames placeName;
    private MutableLiveData<Place> listMutableLiveData;

    public MutableLiveData<Place> getListMutableLiveData() {

        listMutableLiveData = new MutableLiveData<>();
        getPlacesListFromFirebase();
        listMutableLiveData.setValue(place);
        return listMutableLiveData;
    }

    private static RepositoryForPlaces repositoryForPlaces;

    public static RepositoryForPlaces getInstance(PlacesNames placeName) {
        if (repositoryForPlaces == null)
            repositoryForPlaces = new RepositoryForPlaces(placeName);
        return repositoryForPlaces;
    }

    private RepositoryForPlaces(PlacesNames placeName) {
        this.placeName = placeName;
    }


    //Firebaseic stanum em place-i list
    private void getPlacesListFromFirebase() {
         String key = placeName.getName();
         FirebaseDatabase.getInstance().getReference().child("Places").child(key).addListenerForSingleValueEvent( new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 place = dataSnapshot.getValue(Place.class);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });
    }
}
