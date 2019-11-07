package com.ArmGuide.tourapplication.Repositories;

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

public class RepositoryForNotifications {
    private MutableLiveData<List<Tour>> mutableLiveData;

    private static RepositoryForNotifications repositoryForNotifications;

    public static RepositoryForNotifications getInstance() {
        if (repositoryForNotifications == null)
            repositoryForNotifications = new RepositoryForNotifications();
        return repositoryForNotifications;
    }

    private RepositoryForNotifications() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Tour>> getMutableLiveData(){
        getNotificFromFirebase();
        return mutableLiveData;
    }

    private void getNotificFromFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                    .child("Notifications").addValueEventListener(new ValueEventListener() {
                List<Tour> tours = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data: dataSnapshot.getChildren()
                         ) {
                        Tour curTour = data.getValue(Tour.class);
                        tours.add(curTour);
                    }
                    mutableLiveData.setValue(tours);
                    tours.clear();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
