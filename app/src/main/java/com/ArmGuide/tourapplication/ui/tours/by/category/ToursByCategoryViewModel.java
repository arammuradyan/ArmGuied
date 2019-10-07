package com.ArmGuide.tourapplication.ui.tours.by.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.ToursByCategoryRepository;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class ToursByCategoryViewModel extends ViewModel implements ToursByCategoryRepository.OnResponceListener {

    private MutableLiveData<List<Tour>> tours;
    private ToursByCategoryRepository toursByCategoryRepository;

    public ToursByCategoryViewModel() {
        tours = new MutableLiveData<>();
        toursByCategoryRepository=new ToursByCategoryRepository(this);
    }

    public LiveData<List<Tour>> getToursList(String id) {
        toursByCategoryRepository.getToursByCategory(id);
        return tours;
    }

    @Override
    public void onResponceListening(List<Tour> toursList) {
        tours.setValue(toursList);
    }
}