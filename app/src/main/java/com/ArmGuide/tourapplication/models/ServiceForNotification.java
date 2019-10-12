package com.ArmGuide.tourapplication.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ArmGuide.tourapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceForNotification extends Service {

    private ValueEventListener valueEventListenerTours, valueEventListenerPlacesSubscribed;
    private boolean isFirstCall;
    private List<String> placesSubscribed;
    private Tour latestTour, tourUnderCondition;

    @Override
    public void onCreate() {
        super.onCreate();

        placesSubscribed = new ArrayList<>();
        isFirstCall = true;
        /*valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dt : dataSnapshot.getChildren()
                ) {
                    newPerson.setName(dt.child("name").getValue().toString());
                    newPerson.setAge(Integer.valueOf(dt.child("age").getValue().toString()));
                }
                Log.d("MyLog", newPerson.getName() + newPerson.getAge());
                String contentForNot = "New Persons name: " + newPerson.getName() +
                        "\nNew Persons age: " + newPerson.getAge();

                if (!isFirstCall)
                    sendNotification(contentForNot);
                isFirstCall = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };*/

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference refSubPlaces = FirebaseDatabase.getInstance().getReference().child("Tourists").
                child(userId).child("getSubscribedPlacesIds");
        final DatabaseReference refLatestTours = FirebaseDatabase.getInstance().getReference().child("Tours").
                child(userId).child("getSubscribedPlacesIds");

        valueEventListenerTours = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
                    latestTour = snapshot.getValue(Tour.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        valueEventListenerPlacesSubscribed = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
                    placesSubscribed.add(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isFirstCall = true;
      //  FirebaseDatabase.getInstance().getReference().child("people").orderByKey().limitToLast(1).addValueEventListener(valueEventListener);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //FirebaseDatabase.getInstance().getReference().child("people").orderByKey().limitToLast(1).removeEventListener(valueEventListener);
        Log.d("MyLog", "onDestroy worked");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendNotification(String content) {
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceForNotification.this, "channelID");
        builder
                .setContentTitle("New mail from ArmGuide")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_touch_app_black_24dp)
                .setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) ServiceForNotification.this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void combinePlaceTour(){

        String placeName = latestTour.getPlaceName();
        for (String subscribedPlace:placesSubscribed
             ) {
            if(placeName.equals(subscribedPlace)){
                tourUnderCondition = latestTour;
                break;
            }
        }
    }
}
