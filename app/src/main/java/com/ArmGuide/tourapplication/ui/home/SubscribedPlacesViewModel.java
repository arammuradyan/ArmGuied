package com.ArmGuide.tourapplication.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForSubToursCriteria;
import com.ArmGuide.tourapplication.Repositories.RepositoryForSubscribedPlacesNames;
import com.ArmGuide.tourapplication.Repositories.RepositoryForUserState;
import com.ArmGuide.tourapplication.models.Filter;
import com.ArmGuide.tourapplication.models.UserState;

import java.util.List;

public class SubscribedPlacesViewModel extends ViewModel {

    public LiveData<List<String>> getLiveData(){
        return  RepositoryForSubscribedPlacesNames.getInstance().getLiveData();
    }

    public LiveData<UserState> getState() {
        return RepositoryForUserState.getInstance().getMutableLiveData();
    }

    public LiveData<List<Filter>> getFilters(){
        return  RepositoryForSubToursCriteria.getInstance().getMutableLiveData();
    }

}
