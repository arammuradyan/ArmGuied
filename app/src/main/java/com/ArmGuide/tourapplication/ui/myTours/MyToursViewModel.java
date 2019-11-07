package com.ArmGuide.tourapplication.ui.myTours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.MyToursRepository;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.models.Tourist;

import java.util.List;

public class MyToursViewModel extends ViewModel {

    private MyToursRepository myToursRepository;

    public MyToursViewModel() {
       myToursRepository=new MyToursRepository();
    }


    public LiveData<List<Tour>> getCompanyToursList(){
        return myToursRepository.getCompanyTours();
    }

    public LiveData<List<Tour>> getTouristsToursList(){
        return myToursRepository.getTouristsTours();
    }

    public LiveData<List<Tourist>> getTouristsList(String tourId){
        return myToursRepository.getTouristsList(tourId);
    }

    public void deleteTourFromCompany(Tour tour){
        myToursRepository.deleteTourFromCompany(tour);
    }

    public void deleteTourFromTourist(String tourId){
        myToursRepository.deleteTourFromTourist(tourId);
    }

}