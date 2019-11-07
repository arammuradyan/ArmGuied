package com.ArmGuide.tourapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ArmGuide.tourapplication.Repositories.RepositoryForUserState;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.Notification.NotificationFragment;
import com.ArmGuide.tourapplication.ui.home.UserStateViewModel;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {

    RepositoryForUserState repositoryForUserState;
    UserStateViewModel userStateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        repositoryForUserState = RepositoryForUserState.getInstance();
        userStateViewModel = ViewModelProviders.of(NotificationActivity.this).get(UserStateViewModel.class);
        getUserState();
        startFragmentFromNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserState();
        startFragmentFromNotification();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateWindmill(NotificationActivity.this);
    }

    private void getUserState() {
        final String key;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            repositoryForUserState.setIntoRep(UserState.NO_REGISTRATED);
            Log.d("state", "inside getUserState method, not reg");

        } else {
            key = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Companies").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isCompany = false;
                    for (DataSnapshot data : dataSnapshot.getChildren()
                    ) {
                        if (data.getKey().equals(key)) {
                            isCompany = true;
                            Log.d("state", "inside getUserState method, iscompany" + isCompany);
                            repositoryForUserState.setIntoRep(UserState.COMPANY);
                            break;
                        }
                    }
                    if (!isCompany) {
                        Log.d("state", "inside getUserState method, iscompany" + isCompany);
                        repositoryForUserState.setIntoRep(UserState.TOURIST);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void startFragmentFromNotification() {
        userStateViewModel.getState().observe(NotificationActivity.this, new Observer<UserState>() {
            @Override
            public void onChanged(final UserState userState) {
                if(userState==UserState.TOURIST){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_notification, new NotificationFragment()).commit();
                }
                else if(userState == UserState.NO_REGISTRATED){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this)
                            .setTitle("Project requirements.")
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .setCancelable(false)
                            .setMessage("To get tours from notification please LogIn as tourist!")
                            .setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(userState == UserState.COMPANY){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this)
                            .setTitle("Project requirements.")
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .setCancelable(false)
                            .setMessage("Your current state is TOUR AGENCY. To get tours from notification please LogIn as tourist!")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

        });
    }

}
