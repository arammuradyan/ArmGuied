package com.ArmGuide.tourapplication.ui.createTour;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ArmGuide.tourapplication.R;


public class ChooseATravelPackageDelete extends Fragment {

    private Button deleteTravelPackage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_choose_atravel_package_delete, container, false);

        return viewGroup;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        deleteTravelPackage=view.findViewById(R.id.delete_travel_package_BTN);
        deleteTravelPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog adb = new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to delete the travel package you selected?")
                        .setIcon(R.drawable.ic_delete_forever_black_24dp).
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








