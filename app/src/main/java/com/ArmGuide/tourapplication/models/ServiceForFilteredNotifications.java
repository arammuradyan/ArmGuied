package com.ArmGuide.tourapplication.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.myTours.MyToursFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceForFilteredNotifications extends Service {

    private SpecialFilteringClass specialFilteringClass;
    private int idNotification;


    @Override
    public void onCreate() {
        super.onCreate();

        idNotification = 1;
        specialFilteringClass = new SpecialFilteringClass();
        Log.d("dobbi", "service created");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getLatestTour();
        getSubscribedTourCriteria();
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


    private void checkBeforeSendingNot(final List<Tour> tours) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("notg", "tours come " + tours.size());
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final List<Tour> finalList = tours;

            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("ToursAlreadySeen").addValueEventListener(new ValueEventListener() {
                boolean isAlreadySend = false;


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()
                    ) {
                        Tour curTour = data.getValue(Tour.class);
                        for (Tour tour : tours
                        ) {
                            if (tour.getId().equals(curTour.getId())) {
                                finalList.remove(tour);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d("notg", "tours remain" + finalList.size());

            if (finalList.size() > 0) {
                for (Tour tour : finalList
                ) {
                    sendNotification(tour);
                    FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("ToursAlreadySeen")
                            .child(tour.getId()).setValue(tour);
                }
            }

        }
    }

    private void sendNotification(Tour tour) {
        NotificationManager notificationManager =
                (NotificationManager) ServiceForFilteredNotifications.this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intentShow = new Intent(ServiceForFilteredNotifications.this, MyToursFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ServiceForFilteredNotifications.this,
                23, intentShow, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceForFilteredNotifications.this, "channelID");
        builder
                .setContentTitle("New tour  to " + tour.getPlaceName())
                .setContentText("You have a new tour at " + tour.getDate() + " by " + tour.getPrice() + " AMD.")
                .setSmallIcon(R.drawable.ic_earth_pictures)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        notificationManager.notify(idNotification, notification);
        idNotification++;


    }

    private void getSubscribedTourCriteria() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("SubscribedToursCriteria")
                    .addValueEventListener(new ValueEventListener() {
                        List<Filter> tourCriteria = new ArrayList<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tourCriteria.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()
                            ) {
                                tourCriteria.add(snapshot.getValue(Filter.class));
                            }
                            if (tourCriteria.size() > 0) {
                                specialFilteringClass.setPlaceNames(tourCriteria);
                                Log.d("dobbi", "placesSubscribed from onDataChange/" + tourCriteria.size());
                            }
                            if (specialFilteringClass.getToursUnderCondition().size() > 0) {
                                checkBeforeSendingNot(specialFilteringClass.getToursUnderCondition());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }


    private void getLatestTour() {
        FirebaseDatabase.getInstance().getReference().child("Tours").orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Tour> tours = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                        ) {
                            tours.add(snapshot.getValue(Tour.class));
                        }
                        specialFilteringClass.setTours(tours);
                        if (specialFilteringClass.getToursUnderCondition().size() > 0) {
                            checkBeforeSendingNot(specialFilteringClass.getToursUnderCondition());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
