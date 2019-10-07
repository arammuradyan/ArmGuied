package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.map.PlaceInfo;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToursByCategoryRepository {
   private List<Tour> tours= new ArrayList<>();
   private DatabaseReference toursDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURS_DATABASE_REFERENCE);
   private OnResponceListener onResponceListener;

    public ToursByCategoryRepository(OnResponceListener onResponceListener) {
        this.onResponceListener = onResponceListener;
    }

    public void getToursByCategory(final String categoryId){
       toursDatabaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
               for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                   Tour tour=toursData.getValue(Tour.class);

                   String categoryIdFromFb=tour.getId();
                   if (categoryId.equals(categoryIdFromFb)){
                       tours.add(tour);
                   }
               }
                   onResponceListener.onResponceListening(tours);
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });

    }

    public interface OnResponceListener{
       void onResponceListening(List<Tour> tours);

       // TODO es clbackov het tal Place objekt@ viewpageric Mapov cuyctalu hamar
     //  void onPlaceResponceListener(PlaceInfo placeInfo);
    }
}
