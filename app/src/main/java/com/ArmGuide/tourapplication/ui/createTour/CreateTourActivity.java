package com.ArmGuide.tourapplication.ui.createTour;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateTourActivity extends AppCompatActivity {

    private CircleImageView image;
    private TextView selectTheDirection;
    private Spinner tourPackageSpinner;
    private TextView tourData_TV;
    private TextView tourData_ET;
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

    //Spineric data vercnelu hamar
    int position;
    String imageUrl;


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


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean transport=includingTransport_CB.isChecked();
                boolean food=indudingFood_CB.isChecked();
                boolean threeLangGuid=threeLanguageGuiding_CB.isChecked();
                boolean vine=vineDegustation_CB.isChecked();
                boolean wifi=freeWifiDuringTour_CB.isChecked();

                String tourCompanyId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String placeName=packages.get(position);
                String date=tourData_ET.getText().toString().trim();
                String moreInformation=moreInformation_ET.getText().toString().trim();

                List<String> touristsIds=new ArrayList<>();
                touristsIds.add("id 1");

                int price=Integer.parseInt(price_ET.getText().toString().trim());

                if(TextUtils.isEmpty(price_ET.getText().toString().trim())){
                    price_ET.setError("name is required");
                    price_ET.requestFocus();
                    return ;
                }
                if(TextUtils.isEmpty(date)){
                    tourData_ET.setError("email is required");
                    tourData_ET.requestFocus();
                    return ;
                }




                DatabaseReference toursReference=FirebaseDatabase.getInstance()
                                                                 .getReference(Constants.TOURS_DATABASE_REFERENCE)
                                                                 .child(tourCompanyId);
                String tourId=toursReference.push().getKey();

                Tour tour=new Tour();
                tour.setId(tourId);
                tour.setTourCompanyId(tourCompanyId);
                tour.setTouristsIds(touristsIds);
                tour.setPlaceName(placeName);
                tour.setDate(date);
                tour.setMoreInfo(moreInformation);
                tour.setPrice(price);
                tour.setTransport(transport);
                tour.setFood(food);
                tour.setThreeLangGuide(threeLangGuid);
                tour.setVineDegustation(vine);
                tour.setWifi(wifi);

                tour.setImgUrl(imageUrl);

                if(tourId!=null)
                toursReference.child(tourId).setValue(tour).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateTourActivity.this,"tour saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateTourActivity.this,"something went wrong try again", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        adapter = new TourPackageSpinerAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tourPackageSpinner.setAdapter(adapter);
        tourPackageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                position=i;

                FirebaseDatabase.getInstance().getReference().child(Constants.PLACES).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.getKey().equals(packages.get(i))){
                            imageUrl =  dataSnapshot.child("imageUrls").child("0").getValue(String.class);
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

        tourData_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                Dialog dialog = new DatePickerDialog(CreateTourActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tourData_ET.setText(String.format("%s / %s / %s", day, month, year));
                    }
                }, year, month, day);

                dialog.show();
            }
        });

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