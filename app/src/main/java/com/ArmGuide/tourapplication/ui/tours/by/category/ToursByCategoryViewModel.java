package com.ArmGuide.tourapplication.ui.tours.by.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.ToursByCategoryRepository;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class ToursByCategoryViewModel extends ViewModel{

    private ToursByCategoryRepository toursByCategoryRepository;

    public ToursByCategoryViewModel() {
        toursByCategoryRepository=new ToursByCategoryRepository();
    }

    public LiveData<List<Tour>> getToursList(String placeName){
        return toursByCategoryRepository.getToursByCategory(placeName);
    }

}