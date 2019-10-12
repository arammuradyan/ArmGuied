package com.ArmGuide.tourapplication.ui.companies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.AllCompaniesRepository;
import com.ArmGuide.tourapplication.models.Company;

import java.util.List;

public class TourCompaniesViewModel extends ViewModel {

    private AllCompaniesRepository allCompaniesRepository;

    public TourCompaniesViewModel() {
        allCompaniesRepository=new AllCompaniesRepository();
    }

    public LiveData<List<Company>> getAllCompaniesList(){
        return allCompaniesRepository.getAllCompanies();
    }
}