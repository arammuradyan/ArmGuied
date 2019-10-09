package com.ArmGuide.tourapplication.ui.myTours;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.view.View.VISIBLE;

public class MyToursFragment extends Fragment {

    private MyToursViewModel myToursViewModel;
    private FloatingActionButton fab;
    private boolean TOUR_AGENCY=true;

    public MyToursFragment(boolean TOUR_AGENCY) {
        this.TOUR_AGENCY = TOUR_AGENCY;
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myToursViewModel = ViewModelProviders.of(this).get(MyToursViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_tours, container, false);

        final TextView textView = root.findViewById(R.id.text_share);
        fab=root.findViewById(R.id.my_tours_fab);
        if(TOUR_AGENCY){
        fab.setVisibility(VISIBLE);}

        myToursViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}