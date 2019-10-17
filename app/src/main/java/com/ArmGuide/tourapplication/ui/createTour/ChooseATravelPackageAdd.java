package com.ArmGuide.tourapplication.ui.createTour;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.ServiceForNotification;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.home.BlankFragment;
import com.ArmGuide.tourapplication.ui.home.UserStateViewModel;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryChooseATravelPackageAadd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChooseATravelPackageAdd extends Fragment {


    private CircleImageView image, companyImage;
    private TextView thePackageYouSelected_title, companiInfo_tv;
    private TextView thePackageYouSelected;
    private TextView title_tourData_TV;
    private TextView tourData_TV;
    private TextView title_price_TV;
    private TextView price_TV;
    private TextView dram;
    private TextView add_to_my_tours_TV;
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

    private UserStateViewModel userStateViewModel;


    private Tour tour;

    public ChooseATravelPackageAdd(Tour tour) {
        this.tour = tour;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_choose_atravel_package_add, container, false);

        return viewGroup;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userStateViewModel = new UserStateViewModel();


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
        add_to_my_tours_TV=view.findViewById(R.id.add_to_my_tours_TV);

        setTourInformation();

        addToMyTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title =" Do you want to add the selected package to your cart?";
                if(add_to_my_tours_TV.getText().toString().equals("DELETE TRAVEL PACKAGE")){
                    title = "Do you want to delete the travel package you selected?";
                }

                AlertDialog adb = new AlertDialog.Builder(getContext())
                        .setTitle(title)
                        .setIcon(R.drawable.ic_add_shopping_cart_black_24dp).
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "AASSSSSSSSS", Toast.LENGTH_SHORT).show();

                                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    Toast.makeText(getActivity(), "You must sign in as tourist to add ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    //deleteTour();
                                }

                            }
                        }).create();
                adb.show();
            }
        });

        changeAddButton();

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



    private void changeAddButton() {
        userStateViewModel.getState().observe(ChooseATravelPackageAdd.this, new Observer<UserState>() {
            @Override
            public void onChanged(UserState state) {
                Log.d("MyLog", "BlankFragment real state:" + state);
                if (state == UserState.COMPANY) {
                        add_to_my_tours_TV.setText("DELETE TRAVEL PACKAGE");
                        addToMyTours.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_forever_black_24dp));
                }
            }
        });
    }
}