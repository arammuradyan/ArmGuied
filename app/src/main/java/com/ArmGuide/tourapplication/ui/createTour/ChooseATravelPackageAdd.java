package com.ArmGuide.tourapplication.ui.createTour;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ArmGuide.tourapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChooseATravelPackageAdd extends Fragment {


    private CircleImageView image;
    private TextView thePackageYouSelected_title;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_choose_atravel_package_add, container, false);

        return viewGroup;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        image = view.findViewById(R.id.image);
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
        moreInformation_TV = view.findViewById(R.id.moreInformation_ET);
        addToMyTours = view.findViewById(R.id.add_to_my_tours_BTN);


        addToMyTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog adb = new AlertDialog.Builder(getContext())
                        .setTitle(" Do you want to add the selected package to your cart?")
                        .setIcon(R.drawable.ic_add_shopping_cart_black_24dp).
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();

                adb.show();


            }


        });


    }

}









