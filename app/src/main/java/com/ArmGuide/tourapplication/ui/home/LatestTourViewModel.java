package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForLatestTour;
import com.ArmGuide.tourapplication.Repositories.RepositoryForSubscribedPlacesNames;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class LatestTourViewModel extends ViewModel {
    private MutableLiveData<Tour> mutableLiveData;
    public LiveData<Tour> getLiveData(){
        if(mutableLiveData==null)
            mutableLiveData = RepositoryForLatestTour.getInstance().getLiveData();
        return mutableLiveData;
    }
}
