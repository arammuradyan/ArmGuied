package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForSubscribedPlacesNames;

import java.util.List;

public class SubscribedPlacesViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableLiveData;
    public LiveData<List<String>> getLiveData(){
        if(mutableLiveData==null)
            mutableLiveData = RepositoryForSubscribedPlacesNames.getInstance().getLiveData();
        return mutableLiveData;
    }
}
