package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterBackPressedViewModel extends ViewModel {
    private MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();
    public void setState(boolean bool){
        mutableLiveData.setValue(bool);
    }
    public MutableLiveData<Boolean> getLiveData(){
        return mutableLiveData;
    }
}
