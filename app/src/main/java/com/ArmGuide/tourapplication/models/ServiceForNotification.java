package com.ArmGuide.tourapplication.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.home.LatestTourViewModel;
import com.ArmGuide.tourapplication.ui.home.SpecialClass;
import com.ArmGuide.tourapplication.ui.home.SubscribedPlacesViewModel;
import com.ArmGuide.tourapplication.ui.myTours.MyToursFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceForNotification extends Service {

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

        /*subscribedPlacesViewModel.getLiveData().observe(ServiceForNotification.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings != null && strings.size() > 0)
                    specialClass.setPlaceNames(strings);
                Log.d("dobbi", "inside observer strings:" + strings);
                if (specialClass.getTourUnderCondition() != null)
                    sendNotification(specialClass.getTourUnderCondition());
            }
        });

        latestTourViewModel.getLiveData().observe(ServiceForNotification.this, new Observer<Tour>() {
            @Override
            public void onChanged(Tour tour) {
                if (tour != null)
                    specialClass.setTour(tour);
                Log.d("dobbi", "inside observer LatestTourPlaceName:" + tour.getPlaceName());
                if (specialClass.getTourUnderCondition() != null)
                    sendNotification(specialClass.getTourUnderCondition());
            }
        });*/

        getLatestTour();
        getSubscribedPlaces();
        // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dobbi", "onDestroy worked");
    }

   /* @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable


    private void checkBeforeSendingNot(final Tour tour) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("ToursShown").child(userId).addValueEventListener(new ValueEventListener() {
                boolean isAlreadySend = false;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()
                    ) {
                        String tourShownKey = data.getKey();
                        if (tour.getId().equals(tourShownKey)) {
                            isAlreadySend = true;
                            break;
                        }
                    }
                    if (!isAlreadySend) {
                        sendNotification(tour);
                        FirebaseDatabase.getInstance().getReference().child("ToursShown").child(userId).
                                child(tour.getId()).setValue(tour.getPlaceName() + "-" + tour.getTourCompany().getCompanyName() + "-" + tour.getDate());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void sendNotification(Tour tour) {
        NotificationManager notificationManager =
                (NotificationManager) ServiceForNotification.this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intentShow = new Intent(ServiceForNotification.this, MyToursFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ServiceForNotification.this,
                23, intentShow, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceForNotification.this, "channelID");
        builder
                .setContentTitle("New tour  to " + tour.getPlaceName())
                .setContentText("You have a new tour at " + tour.getDate() + " by " + tour.getPrice() + " AMD.")
                .setSmallIcon(R.drawable.ic_earth_pictures)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);

    }

    private void getSubscribedPlaces() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("getSubscribedPlacesIds")
                    .addValueEventListener(new ValueEventListener() {
                        List<String> placesSubscribed = new ArrayList<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            placesSubscribed.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()
                            ) {
                                placesSubscribed.add(snapshot.getValue(String.class));
                            }
                            if (placesSubscribed.size() > 0) {
                                specialClass.setPlaceNames(placesSubscribed);
                                Log.d("dobbi", "placesSubscribed from onDataChange/" + placesSubscribed.size());
                            }
                            if (specialClass.getTourUnderCondition() != null) {
                                checkBeforeSendingNot(specialClass.getTourUnderCondition());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }


    private void getLatestTour() {
        FirebaseDatabase.getInstance().getReference().child("Tours").orderByKey().limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Tour latestTour = new Tour();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                        ) {
                            latestTour = snapshot.getValue(Tour.class);
                        }
                        Log.d("dobbi", "latestTourName from onDataChange/" + latestTour.getPlaceName());
                        specialClass.setTour(latestTour);
                        if (specialClass.getTourUnderCondition() != null) {
                            checkBeforeSendingNot(specialClass.getTourUnderCondition());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
