package com.ArmGuide.tourapplication.ui.myTours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyToursViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyToursViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}