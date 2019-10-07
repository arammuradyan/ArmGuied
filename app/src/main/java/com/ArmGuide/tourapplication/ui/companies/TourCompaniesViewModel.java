package com.ArmGuide.tourapplication.ui.companies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TourCompaniesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TourCompaniesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}