package com.ArmGuide.tourapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCompaniesRepository {

    private List<Tour> toursList =new ArrayList<>();
    private MutableLiveData<List<Tour>> tours= new MutableLiveData<>();

    private List<Company> companiesList= new ArrayList<>();
   private MutableLiveData<List<Company>> companies= new MutableLiveData<>();

   private DatabaseReference companiesDatabaseReference= FirebaseDatabase.getInstance()
           .getReference(Constants.COMPANIES_DATABASE_REFERENCE);


    private DatabaseReference toursDatabaseReference= FirebaseDatabase.getInstance()
            .getReference(Constants.TOURS_DATABASE_REFERENCE);


    public AllCompaniesRepository() {
    }

    public LiveData<List<Company>> getAllCompanies(){
        companiesDatabaseReference.keepSynced(true);
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

    public LiveData<List<Tour>> getCompanyToursList(Company company){

        final String compnyId=company.getId();
        toursDatabaseReference.keepSynced(true);
        toursDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toursList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot toursData:dataSnapshot.getChildren()) {
                        Tour tour=toursData.getValue(Tour.class);

                        if(tour.getTourCompany().getId().equals(compnyId)){
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

}
