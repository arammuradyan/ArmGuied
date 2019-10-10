package com.ArmGuide.tourapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.MyToursRepository;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class StateViewModel extends ViewModel {

    private MutableLiveData<Boolean> userState=new MutableLiveData<>();

    public StateViewModel() {
    }


    public void setState(boolean TOUR_AGENCY){
        userState.postValue(TOUR_AGENCY);
    }

    public LiveData<Boolean> getState(){
        return userState;
    }
}