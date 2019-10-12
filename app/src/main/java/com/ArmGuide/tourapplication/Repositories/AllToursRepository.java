package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllToursRepository {

   private List<Tour> allToursList= new ArrayList<>();
   private MutableLiveData<List<Tour>> allTours= new MutableLiveData<>();

   private DatabaseReference companiesDatabaseReference= FirebaseDatabase.getInstance().getReference(Constants.TOURS_DATABASE_REFERENCE);


    public AllToursRepository() {
    }

    public LiveData<List<Tour>> getAllTours(){
        companiesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              // companiesList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot companyData:dataSnapshot.getChildren()) {
                        Tour tour=companyData.getValue(Tour.class);
                        allToursList.add(tour);
                    }
               allTours.postValue(allToursList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
return allTours;
    }

}
