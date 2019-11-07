package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.models.Filter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;

public class RepositoryForSubToursCriteria {
    private MutableLiveData<List<Filter>> mutableLiveData;
    private static RepositoryForSubToursCriteria repositoryForSubToursCriteria;
    public static RepositoryForSubToursCriteria getInstance(){
        if(repositoryForSubToursCriteria==null)
            repositoryForSubToursCriteria = new RepositoryForSubToursCriteria();
        return repositoryForSubToursCriteria;
    }
    private RepositoryForSubToursCriteria(){
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Filter>> getMutableLiveData(){
        getFromFirebase();
        return mutableLiveData;
    }


    private void getFromFirebase(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                    .child("SubscribedToursCriteria").addValueEventListener(new ValueEventListener() {

                        List<Filter> filters = new ArrayList<>();
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    filters.clear();
                    for (DataSnapshot data: dataSnapshot.getChildren()
                         ) {
                        Filter curFilter = data.getValue(Filter.class);
                        filters.add(curFilter);
                    }
                    mutableLiveData.setValue(filters);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
