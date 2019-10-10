package com.ArmGuide.tourapplication.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repository.RepositoryForPlaces;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlacesNames;

public class ViewModelBlankFragment extends ViewModel {

    private LiveData<Place> placeLiveData;

    public LiveData<Place> getPlaceLiveData(PlacesNames placeName) {
        if (placeLiveData == null)
            placeLiveData = RepositoryForPlaces.getInstance(placeName).getListMutableLiveData();
        return placeLiveData;
    }
}
