package com.ArmGuide.tourapplication.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ArmGuide.tourapplication.models.UserState;

public class RepositoryForUserState {
    private static RepositoryForUserState repository;
    private MutableLiveData<UserState> mutableLiveData;

    public static RepositoryForUserState getInstance() {
        if (repository == null)
            repository = new RepositoryForUserState();
        return repository;
    }

    private RepositoryForUserState() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void setIntoRep(UserState state) {
        mutableLiveData.setValue(state);
        Log.d("state", "repo value, setState:mutableLifeData: " + mutableLiveData.getValue());

    }

    public MutableLiveData<UserState> getMutableLiveData() {
        Log.d("state", "repo value, getState:mutableLifeData: " + mutableLiveData.getValue());
        return mutableLiveData;
    }
}
