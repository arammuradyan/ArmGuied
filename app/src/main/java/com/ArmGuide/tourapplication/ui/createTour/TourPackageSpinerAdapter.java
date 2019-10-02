package com.ArmGuide.tourapplication.ui.createTour;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TourPackageSpinerAdapter extends ArrayAdapter<String> {

    private List<String> tourPackages;

    public TourPackageSpinerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        tourPackages = new ArrayList<>();
    }

    public void addTourPackages(List<String> tourPackages) {
        this.tourPackages.clear();
        this.tourPackages.addAll(tourPackages);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return tourPackages.get(position);
    }

    @Override
    public int getCount() {
        return tourPackages.size();
    }
}
