package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Company;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCompaniesRepository {

   private List<Company> companiesList= new ArrayList<>();
   private MutableLiveData<List<Company>> companies= new MutableLiveData<>();

   private DatabaseReference companiesDatabaseReference= FirebaseDatabase.getInstance().getReference(Constants.COMPANIES_DATABASE_REFERENCE);


    public AllCompaniesRepository() {
    }

    public LiveData<List<Company>> getAllCompanies(){
        companiesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              // companiesList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot companyData:dataSnapshot.getChildren()) {
                        Company company=companyData.getValue(Company.class);
                        companiesList.add(company);
                    }
               companies.postValue(companiesList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
return companies;
    }

}
