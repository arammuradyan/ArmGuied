package com.ArmGuide.tourapplication.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.NotificationActivity;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.myTours.MyToursFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
        Log.d("xncor", "service created");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("xncor", "onDestroy onStartCommand");

        getLatestTour();
        getSubscribedTourCriteria();
        // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("xncor", "onDestroy worked");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable


    private void checkBeforeSendingNot(final List<Tour> tours) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("polo", "inside checkBeforeSendingNot, tours: " + tours.size());
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("ToursAlreadySeen")
                    .addValueEventListener(new ValueEventListener() {

                        List<Tour> alreadySeen = new ArrayList<>();
                        List<Tour> finalTours = new ArrayList<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()
                            ) {
                                alreadySeen.add(data.getValue(Tour.class));
                            }
                            finalTours = specialFilteringClass.setToursToFilter(alreadySeen, tours);
                            Log.d("polo", "inside checkBeforeSendingNot, finalTours: " + finalTours.size());
                            Log.d("polo", "inside checkBeforeSendingNot, alreadySeen: " + alreadySeen.size());


                            if (finalTours.size() > 0) {
                                for (Tour t : finalTours
                                ) {
                                    FirebaseDatabase.getInstance().getReference().child("Tourists").child(userId).child("ToursAlreadySeen")
                                            .child(t.getId()).setValue(t);
                                }
                                sendToNotifications(finalTours,userId);
                                sendNotification(finalTours);
                            }
                            alreadySeen.clear();
                            finalTours.clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }


    private void sendToNotifications(List<Tour> tours, String userId) {
        if (tours.size() > 0) {
            for (Tour t : tours
            ) {
                FirebaseDatabase.getInstance().getReference().child("Tourists")
                        .child(userId).child("Notifications").child(t.getId()).setValue(t);
            }
        }
    }


    private void sendNotification(List<Tour> tours) {
        NotificationManager notificationManager =
                (NotificationManager) ServiceForFilteredNotifications.this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intentShow = new Intent(ServiceForFilteredNotifications.this, NotificationActivity.class);
        intentShow.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // intentShow.putExtra("notification", "notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(ServiceForFilteredNotifications.this,
                23, intentShow, PendingIntent.FLAG_UPDATE_CURRENT);

        String contentTitle = tours.size() == 1 ? "You have 1 new awesome tour to " + tours.get(0).getPlaceName() + "!" : "You have " + tours.size() + " new awesome tours!";

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (Tour t : tours
        ) {
            inboxStyle.addLine(t.getPlaceName() + ", agency: " + t.getTourCompany().getCompanyName() + ", price: " + t.getPrice());
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceForFilteredNotifications.this, "channelID");
        builder
                .setContentTitle(contentTitle)
                .setContentText("We found tours according to " +
                        "Your order. Click to get all the details!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.world))
                .setSmallIcon(R.drawable.ic_earth_pictures)
                .setWhen(System.currentTimeMillis())
                .setStyle(inboxStyle)
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
                            Log.d("polo", "inside getSubscribedTourCriteria, observer, tourCriteria: " + tourCriteria.size());

                            if (tourCriteria.size() > 0) {
                                specialFilteringClass.setCriteria(tourCriteria);
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

                    List<Tour> tours = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()
                        ) {
                            Log.d("polo", "inside checkBeforeSendingNot, tours: " + data.getValue());

                            tours.add(data.getValue(Tour.class));
                        }
                        specialFilteringClass.setTours(tours);
                        if (specialFilteringClass.getToursUnderCondition().size() > 0) {
                            checkBeforeSendingNot(specialFilteringClass.getToursUnderCondition());
                        }
                        tours.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
