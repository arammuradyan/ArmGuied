package com.ArmGuide.tourapplication.ui.createTour;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateTourActivity extends AppCompatActivity {

    private CircleImageView image;
    private TextView selectTheDirection;
    private Spinner tourPackageSpinner;
    private TextView tourData_TV;
    private EditText tourData_ET;
    private TextView price_TV;
    private EditText price_ET;
    private TextView dram;
    private TextView includingTransport_TV;
    private TextView indudingFood_TV;
    private TextView threeLanguageGuiding_TV;
    private TextView vineDegustation_TV;
    private TextView freeWifiDuringTour_TV;
    private CheckBox includingTransport_CB;
    private CheckBox indudingFood_CB;
    private CheckBox threeLanguageGuiding_CB;
    private CheckBox vineDegustation_CB;
    private CheckBox freeWifiDuringTour_CB;
    private TextView moreInformation_TV;
    private EditText moreInformation_ET;
    private Button save;
    private TourPackageSpinerAdapter adapter;


    private DatabaseReference placesDatabaseReference;
    private ChildEventListener placeChildEventListener;
    final List<String> packages = new ArrayList<>();

    private DatabaseReference databaseVisitors;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tour);


        image = findViewById(R.id.image);
        selectTheDirection = findViewById(R.id.selectTheDirection);
        tourPackageSpinner = findViewById(R.id.touristDestinations);
        tourData_TV = findViewById(R.id.tourData_TV);
        tourData_ET = findViewById(R.id.tourData_ET);
        price_TV = findViewById(R.id.price_TV);
        price_ET = findViewById(R.id.price_ET);
        dram = findViewById(R.id.dram);
        includingTransport_TV = findViewById(R.id.includingTransport_TV);
        indudingFood_TV = findViewById(R.id.indudingFood_TV);
        threeLanguageGuiding_TV = findViewById(R.id.threeLanguageGuiding_TV);
        vineDegustation_TV = findViewById(R.id.vineDegustation_TV);
        freeWifiDuringTour_TV = findViewById(R.id.freeWifiDuringTour_TV);
        includingTransport_CB = findViewById(R.id.includingTransport_CB);
        indudingFood_CB = findViewById(R.id.indudingFood_CB);
        threeLanguageGuiding_CB = findViewById(R.id.threeLanguageGuiding_CB);
        vineDegustation_CB = findViewById(R.id.vineDegustation_CB);
        freeWifiDuringTour_CB = findViewById(R.id.freeWifiDuringTour_CB);
        moreInformation_TV = findViewById(R.id.moreInformation_TV);
        moreInformation_ET = findViewById(R.id.moreInformation_ET);
        save=findViewById(R.id.save);


        adapter = new TourPackageSpinerAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tourPackageSpinner.setAdapter(adapter);
        tourPackageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                FirebaseDatabase.getInstance().getReference().child(Constants.PLACES).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.getKey().equals(packages.get(i))){
                            String imageUrl =  dataSnapshot.child("imageUrls").child("0").getValue(String.class);
                            Picasso.get().load(imageUrl).into(image);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initPlacesFirebase();

    }


    @Override
    protected void onStop() {
        super.onStop();
        placesDatabaseReference.removeEventListener(placeChildEventListener);
    }

    private void initPlacesFirebase() {
        placesDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.PLACES);
        placeChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                packages.add(dataSnapshot.getKey());
                adapter.addTourPackages(packages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        placesDatabaseReference.addChildEventListener(placeChildEventListener);
    }
}
