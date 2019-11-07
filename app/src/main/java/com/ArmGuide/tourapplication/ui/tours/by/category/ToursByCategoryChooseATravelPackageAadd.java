package com.ArmGuide.tourapplication.ui.tours.by.category;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.Constants;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.StateViewModel;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.models.Tourist;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.home.UserStateViewModel;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ToursByCategoryChooseATravelPackageAadd extends Fragment {


    private CircleImageView image, companyImage;
    private TextView thePackageYouSelected_title, companiInfo_tv;
    private TextView thePackageYouSelected;
    private TextView title_tourData_TV;
    private TextView tourData_TV;
    private TextView title_price_TV;
    private TextView price_TV;
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
    private TextView title_moreInfo_TV;
    private TextView moreInformation_TV;
    private Button addToMyTours;
    private TextView add_to_my_tours_TV;
    private UserStateViewModel userStateViewModel;


    private Tour tour;
    private StateViewModel stateViewModel;

    public ToursByCategoryChooseATravelPackageAadd(Tour tour) {
        this.tour = tour;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_atravel_package_add, container, false);

        if (getActivity() != null) {
            stateViewModel = ViewModelProviders.of(getActivity()).get(StateViewModel.class);
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userStateViewModel = new UserStateViewModel();

        initViews(view);
        setTourInformation();


        stateViewModel.getState().observe(ToursByCategoryChooseATravelPackageAadd.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    addToMyTours.setVisibility(View.GONE);

                } else {
                    addToMyTours.setVisibility(View.VISIBLE);
                }
            }
        });


        // addToMyTours.setVisibility(View.VISIBLE);
        addToMyTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog adb = new AlertDialog.Builder(getContext())
                        .setMessage(" Do you want to add the selected package to your cart?")
                        .setIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    Toast.makeText(getActivity(), "You must sign in as tourist to add ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    stateViewModel.getState().observe(ToursByCategoryChooseATravelPackageAadd.this, new Observer<Boolean>() {
                                        @Override
                                        public void onChanged(Boolean aBoolean) {
                                            if (aBoolean) {
                                                Toast.makeText(getActivity(), "only tourists can add", Toast.LENGTH_SHORT).show();
                                            } else {
                                                saveTour();
                                            }
                                        }
                                    });

                                }

                            }
                        }).create();
                adb.show();
            }
        });

    }


    private void initViews(View view) {

        companyImage = view.findViewById(R.id.image2);
        image = view.findViewById(R.id.image);

        companiInfo_tv = view.findViewById(R.id.company_name);

        thePackageYouSelected_title = view.findViewById(R.id.thePackageYouSelected_title);
        thePackageYouSelected = view.findViewById(R.id.thePackageYouSelected);
        title_tourData_TV = view.findViewById(R.id.title_tourData_TV);
        tourData_TV = view.findViewById(R.id.tourData_TV);
        title_price_TV = view.findViewById(R.id.title_price_TV);
        price_TV = view.findViewById(R.id.price_TV);
        dram = view.findViewById(R.id.dram);
        includingTransport_TV = view.findViewById(R.id.includingTransport_TV);
        indudingFood_TV = view.findViewById(R.id.indudingFood_TV);
        threeLanguageGuiding_TV = view.findViewById(R.id.threeLanguageGuiding_TV);
        vineDegustation_TV = view.findViewById(R.id.vineDegustation_TV);
        freeWifiDuringTour_TV = view.findViewById(R.id.freeWifiDuringTour_TV);

        includingTransport_CB = view.findViewById(R.id.includingTransport_CB);
        indudingFood_CB = view.findViewById(R.id.indudingFood_CB);
        threeLanguageGuiding_CB = view.findViewById(R.id.threeLanguageGuiding_CB);
        vineDegustation_CB = view.findViewById(R.id.vineDegustation_CB);
        freeWifiDuringTour_CB = view.findViewById(R.id.freeWifiDuringTour_CB);

        title_moreInfo_TV = view.findViewById(R.id.title_moreInfo_TV);
        moreInformation_TV = view.findViewById(R.id.moreInformation_TV);
        addToMyTours = view.findViewById(R.id.add_to_my_tours_BTN);
        add_to_my_tours_TV = view.findViewById(R.id.add_to_my_tours_TV);

    }


    private void setTourInformation() {
        if (tour.getTourCompany().getAvatarUrl() != null) {
            if (!tour.getTourCompany().getAvatarUrl().isEmpty()) {
                Picasso.get().load(tour.getTourCompany().getAvatarUrl())
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(companyImage);
            }
        }

        if (tour.getImgUrl() != null) {
            if (!tour.getImgUrl().isEmpty()) {
                Picasso.get().load(tour.getImgUrl())
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(image);
            }
        }
        String companyInfo = tour.getTourCompany().getCompanyName() + "\n"
                + tour.getTourCompany().getPhoneNumber() + "\n"
                + tour.getTourCompany().getAddress() + "\n"
                + tour.getTourCompany().getEmail() + "\n"
                + tour.getTourCompany().getWebUrl();
        companiInfo_tv.setText(companyInfo);

        thePackageYouSelected.setText(tour.getPlaceName());
        tourData_TV.setText(String.format("%s - %s", tour.getDate(), tour.getEndDate()));
        price_TV.setText(String.valueOf(tour.getPrice()));
        moreInformation_TV.setText(tour.getMoreInfo());
        includingTransport_CB.setChecked(tour.isTransport());
        indudingFood_CB.setChecked(tour.isFood());
        threeLanguageGuiding_CB.setChecked(tour.isThreeLangGuide());
        vineDegustation_CB.setChecked(tour.isVineDegustation());
        freeWifiDuringTour_CB.setChecked(tour.isWifi());

        includingTransport_CB.setClickable(false);
        indudingFood_CB.setClickable(false);
        threeLanguageGuiding_CB.setClickable(false);
        vineDegustation_CB.setClickable(false);
        freeWifiDuringTour_CB.setClickable(false);
    }


    private void saveTour() {
        String touristId = "";

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            touristId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Toast.makeText(getActivity(), "touristId " + touristId, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "You must sign in as tourist to add ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        final DatabaseReference touristReferance = FirebaseDatabase.getInstance().getReference(Constants.TOURISTS_DATABASE_REFERENCE);

        touristReferance.child(touristId).child("tours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), "on DATA CHANGE", Toast.LENGTH_SHORT).show();
                List<Tour> toursfromFB = new ArrayList<>();
                // if (dataSnapshot.exists()){
                for (DataSnapshot toursFB : dataSnapshot.getChildren()) {
                    Tour tourFB = toursFB.getValue(Tour.class);
                    toursfromFB.add(tourFB);
                }
                for (int i = 0; i < toursfromFB.size(); i++) {
                    if (tour.getId().equals(toursfromFB.get(i).getId())) {

                        Toast.makeText(getActivity(), "ID _" + toursfromFB.get(i).getId() +
                                " Cant add, you allready added " + tour.getId(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                toursfromFB.add(tour);

                final String touristId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                touristReferance
                        .child(touristId)
                        .child("tours")
                        .setValue(toursfromFB).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveTouristIdInTour();

                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // }
//            else{
//                toursfromFB.add(tour);
//                String touristId= FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                touristReferance
//                        .child(touristId)
//                        .child("tours")
//                        .setValue(toursfromFB).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getActivity(), "Added",Toast.LENGTH_SHORT).show();
//                    return;
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveTouristIdInTour() {

        final String touristId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference toursReference = FirebaseDatabase.getInstance()
                .getReference(Constants.TOURS_DATABASE_REFERENCE);
        toursReference
                .child(tour.getId())
                .child("touristsIds")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> touristsIdsFB = new ArrayList<>();
                        for (DataSnapshot toursistIds : dataSnapshot.getChildren()) {
                            String id = toursistIds.getValue(String.class);
                            touristsIdsFB.add(id);
                        }
                        touristsIdsFB.add(touristId);

                        toursReference
                                .child(tour.getId())
                                .child("touristsIds")
                                .setValue(touristsIdsFB).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Id aded to tour", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void deleteTour() {
        FirebaseDatabase.getInstance().getReference(Constants.TOURS_DATABASE_REFERENCE)
                .child(tour.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Tour deleted", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

}









