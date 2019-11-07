package com.ArmGuide.tourapplication.ui.Notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ArmGuide.tourapplication.Repositories.RepositoryForNotifications;
import com.ArmGuide.tourapplication.models.Tour;

import java.util.List;

public class NotificationsViewModel extends ViewModel {
    private MutableLiveData<List<Tour>> mutableLiveData = new MutableLiveData<>();

    public LiveData<List<Tour>> getLiveData(){
        mutableLiveData = RepositoryForNotifications.getInstance().getMutableLiveData();
        return mutableLiveData;
    }
}
