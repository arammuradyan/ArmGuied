package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Tour;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToursByCategoryRepository {
   private List<Tour> toursList= new ArrayList<>();
   private MutableLiveData<List<Tour>> tours= new MutableLiveData<>();

   private DatabaseReference toursDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURS_DATABASE_REFERENCE);


    public ToursByCategoryRepository() {
    }

    public LiveData<List<Tour>> getToursByCategory(final String placeName){
        toursDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                        Tour tour=toursData.getValue(Tour.class);

                        String placeNameFromFb=tour.getPlaceName();
                        if (placeName.equals(placeNameFromFb)){
                            toursList.add(tour);
                        }
                    }
                   tours.postValue(toursList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
return tours;
    }

}
