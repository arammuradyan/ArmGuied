package com.ArmGuide.tourapplication.ui.allTours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllToursViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllToursViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}