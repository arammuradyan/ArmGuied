package com.ArmGuide.tourapplication.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForUserState;
import com.ArmGuide.tourapplication.models.UserState;

public class UserStateViewModel extends ViewModel {

    private MutableLiveData<UserState> mutableLiveData = new MutableLiveData<>();

    public UserStateViewModel() {
    }


    public void setState(UserState state) {
        if(mutableLiveData==null)


        Log.d("state","viewHolder value, setState: "+state);
        mutableLiveData.setValue(state);

    }

    public LiveData<UserState> getState() {
        mutableLiveData = RepositoryForUserState.getInstance().getMutableLiveData();
        Log.d("state","viewHolder value: "+mutableLiveData.getValue());
        return mutableLiveData;
    }
}