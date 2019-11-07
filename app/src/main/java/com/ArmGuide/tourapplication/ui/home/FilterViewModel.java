package com.ArmGuide.tourapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.Repositories.RepositoryForSubToursCriteria;
import com.ArmGuide.tourapplication.models.Filter;

import java.util.List;

public class FilterViewModel extends ViewModel {
    private MutableLiveData<List<Filter>> mutableLiveData;

    public LiveData<List<Filter>> getLiveData(){
        if(mutableLiveData!=null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData = RepositoryForSubToursCriteria.getInstance().getMutableLiveData();
        return mutableLiveData;
    }
}
