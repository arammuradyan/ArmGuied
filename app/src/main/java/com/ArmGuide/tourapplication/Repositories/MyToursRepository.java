package com.ArmGuide.tourapplication.Repositories;



import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.models.Tourist;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyToursRepository {

   private List<Tour> toursList =new ArrayList<>();
   private List<Tourist> touristsList =new ArrayList<>();
   private MutableLiveData<List<Tour>> tours= new MutableLiveData<>();
   private MutableLiveData<List<Tourist>> tourists= new MutableLiveData<>();

   private String touristIdForUpdateToursList="";


   private DatabaseReference toursDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURS_DATABASE_REFERENCE);
   private DatabaseReference touristsDatabaseReference= FirebaseDatabase.getInstance()
                                                     .getReference(Constants.TOURISTS_DATABASE_REFERENCE);


    public MyToursRepository() {
    }


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

                        //if(tour.getTourCompany()!=null)
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

        touristsDatabaseReference.keepSynced(true);
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


    public LiveData<List<Tourist>> getTouristsList(String tourId){
        toursDatabaseReference
                .child(tourId)
                .child("touristsIds")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {


                            final List<String> touristsIds = new ArrayList<>();
                            touristsIds.clear();
                            for (DataSnapshot idis : dataSnapshot.getChildren()) {
                                String id = idis.getValue(String.class);
                                touristsIds.add(id);
                            }
                            touristsDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    List<Tourist> allTourists=new ArrayList<>();
                                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                                        Tourist tourist= data.getValue(Tourist.class);
                                        allTourists.add(tourist);
                                    }
                                     touristsList.clear();
                                    for (int i = 0; i <allTourists.size() ; i++) {
                                        for (int j = 0; j <touristsIds.size() ; j++) {
                                            if(allTourists.get(i).getId().equals(touristsIds.get(j))){
                                              touristsList.add(allTourists.get(i));
                                            }

                                        }
                                    }

                                    tourists.postValue(touristsList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return tourists;
    }

    public void deleteTourFromCompany(final Tour tour){
        final String tourId=tour.getId();
        final List<String> touristsIds1=tour.getTouristsIds();

        toursDatabaseReference.child(tourId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            notifyTourists(touristsIds1,tourId);

            }
        });
    }

    private void notifyTourists(final List<String> touristsIds, final String tourId){
        for ( int i = 0; i <touristsIds.size() ; i++) {

            touristIdForUpdateToursList=touristsIds.get(i);

            Query touristQueryByUid =
                    touristsDatabaseReference
                    .orderByChild("id")
                    .equalTo(touristIdForUpdateToursList);

            touristQueryByUid.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<Tour> tours=new ArrayList<>();
                    List<Tour> toursUpdate=new ArrayList<>();
                    Tourist tourist=dataSnapshot.getChildren().iterator().next().getValue(Tourist.class);

                        tours.addAll(tourist.getTours());

                    for (int i = 0; i <tours.size() ; i++) {

                        if(tours.get(i).getId().equals(tourId)){
                         Tour updatedTour= tours.get(i);
                         updatedTour.setMoreInfo("COMPANY HAS DELETED THIS TOUR");
                         toursUpdate.add(updatedTour);
                        }else{
                            toursUpdate.add(tours.get(i));
                        }
                    }
                    touristsDatabaseReference
                              .child(touristIdForUpdateToursList)
                            .child("tours")
                            .setValue(toursUpdate);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    public void deleteTourFromTourist(final String tourId){

        Query touristQueryByUid =
                touristsDatabaseReference
                        .orderByChild("id")
                        .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        touristQueryByUid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Tour> tours=new ArrayList<>();
                List<Tour> toursUpdate=new ArrayList<>();
                Tourist tourist=dataSnapshot.getChildren().iterator().next().getValue(Tourist.class);

                tours.addAll(tourist.getTours());

                for (int i = 0; i <tours.size() ; i++) {
                    if(!tours.get(i).getId().equals(tourId)){
                        toursUpdate.add(tours.get(i));
                    }
                }
                touristsDatabaseReference
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("tours")
                        .setValue(toursUpdate);

                deleteTouristIdFromTour(tourId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteTouristIdFromTour(String tourid){

    }
}
