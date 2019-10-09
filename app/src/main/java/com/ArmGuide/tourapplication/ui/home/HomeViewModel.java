package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForPlaces;
import com.ArmGuide.tourapplication.models.Place;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Place>> mutableLiveData;

    public LiveData<List<Place>> getLiveData(){
        if(mutableLiveData==null)
            mutableLiveData = RepositoryForPlaces.getInstance().getListMutableLiveData();
        return mutableLiveData;
    }
}