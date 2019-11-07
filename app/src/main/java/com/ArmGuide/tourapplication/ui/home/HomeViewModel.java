package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForPlaces;
import com.ArmGuide.tourapplication.models.Place;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Place>> mutableLiveData = new MutableLiveData<>();

    public LiveData<List<Place>> getLiveData() {
        mutableLiveData = RepositoryForPlaces.getInstance().getListMutableLiveData();
        return mutableLiveData;
    }
}