package com.ArmGuide.tourapplication.ui.allTours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.AllCompaniesRepository;
import com.ArmGuide.tourapplication.Repositories.AllToursRepository;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class AllToursViewModel extends ViewModel {

    private AllToursRepository allToursRepository;

    public AllToursViewModel() {
        allToursRepository=new AllToursRepository();
    }

    public LiveData<List<Tour>> getAllToursList(){
        return allToursRepository.getAllTours();
    }
}