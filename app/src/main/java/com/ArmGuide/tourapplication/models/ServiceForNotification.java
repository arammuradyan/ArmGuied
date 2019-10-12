package com.ArmGuide.tourapplication.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.home.LatestTourViewModel;
import com.ArmGuide.tourapplication.ui.home.SpecialClass;
import com.ArmGuide.tourapplication.ui.home.SubscribedPlacesViewModel;

import java.util.List;

public class ServiceForNotification extends LifecycleService {

    private SpecialClass specialClass;
    private SubscribedPlacesViewModel subscribedPlacesViewModel;
    private LatestTourViewModel latestTourViewModel;

    @Override
    public void onCreate() {
        super.onCreate();

        subscribedPlacesViewModel = new SubscribedPlacesViewModel();
        latestTourViewModel = new LatestTourViewModel();
        specialClass = new SpecialClass();
        Log.d("dobbi", "service created");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        subscribedPlacesViewModel.getLiveData().observe(ServiceForNotification.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings != null && strings.size()>0)
                    specialClass.setPlaceNames(strings);
                Log.d("dobbi", "inside observer strings:" + strings);
                if(specialClass.getTourUnderCondition()!=null)
                    sendNotification(specialClass.getTourUnderCondition());
            }
        });

        latestTourViewModel.getLiveData().observe(ServiceForNotification.this, new Observer<Tour>() {
            @Override
            public void onChanged(Tour tour) {
                if(tour!=null)
                    specialClass.setTour(tour);
                Log.d("dobbi", "inside observer LatestTourPlaceName:" + tour.getPlaceName());
                if(specialClass.getTourUnderCondition()!=null)
                    sendNotification(specialClass.getTourUnderCondition());
            }
        });


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dobbi", "onDestroy worked");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Nullable


    private void sendNotification(Tour tour) {
        Log.d("dobbi", "in notif-name "+tour.getPlaceName() + ",date: "+tour.getDate());

        NotificationManager notificationManager =
                (NotificationManager) ServiceForNotification.this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceForNotification.this, "channelID");
        builder
                .setContentTitle("New tour  to " + tour.getPlaceName())
                .setContentText("You have a new tour at " + tour.getDate() + " by " + tour.getPrice() + " AMD.")
                .setSmallIcon(R.drawable.ic_touch_app_black_24dp)
                .setAutoCancel(true);
        Notification notification = builder.build();

        notificationManager.notify(1, notification);
    }
}
