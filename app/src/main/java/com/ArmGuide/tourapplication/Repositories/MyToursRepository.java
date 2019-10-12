package com.ArmGuide.tourapplication.Repositories;



import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Tour;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyToursRepository {

   private List<Tour> toursList =new ArrayList<>();
   private MutableLiveData<List<Tour>> tours= new MutableLiveData<>();


   private DatabaseReference toursDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURS_DATABASE_REFERENCE);
   private DatabaseReference touristsDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURISTS_DATABASE_REFERENCE);


    public MyToursRepository() {
    }

   /* public LiveData<List<Tour>> getMyTours(){
       if(FirebaseAuth.getInstance().getCurrentUser()==null){
           return null;
       }

       final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
       toursDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              toursList.clear();
               if(dataSnapshot.exists()){
                   for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                       Tour tour=toursData.getValue(Tour.class);
                       toursList.add(tour);
                       Log.d(TAG, "onDataChange: COMPANYYI HAMAR SET AREC");
                   }

                   tours.postValue(toursList);
               }else{
                   Log.d(TAG, "onDataChange: mtav else");

                   DatabaseReference touristReference= FirebaseDatabase.getInstance()
                           .getReference(Constants.TOURISTS_DATABASE_REFERENCE);

                   DatabaseReference toursReference=touristReference.child(userId).child("tours");

                   toursReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           toursList.clear();
                           if(dataSnapshot.exists()){
                               for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                                   Tour tour=toursData.getValue(Tour.class);
                                   toursList.add(tour);
                               }
                               tours.postValue(toursList);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                          // touristi tourer@ error
                       }
                   });
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               // companiayi tourer@ error
           }
       });
return tours;
    }*/


    public LiveData<List<Tour>> getCompanyTours(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            return null;
        }
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        toursDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toursList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                        Tour tour=toursData.getValue(Tour.class);
                        if(tour.getTourCompany().getId().equals(userId)){
                        toursList.add(tour);}
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

    public LiveData<List<Tour>> getTouristsTours(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            return null;
        }
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        touristsDatabaseReference.child(userId).child("tours").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toursList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                        Tour tour=toursData.getValue(Tour.class);
                        toursList.add(tour);
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
