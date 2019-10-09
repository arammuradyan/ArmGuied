package com.ArmGuide.tourapplication.ui.tours.by.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.createTour.ChooseATravelPackageAdd;
import com.ArmGuide.tourapplication.ui.createTour.CreateTourActivity;

import java.util.ArrayList;
import java.util.List;


public class ToursByCategoryFragment extends Fragment implements ToursRecyclerViewAdapter.OnToursViewHolderCLickListener {

    private RecyclerView recyclerView;
    private ToursRecyclerViewAdapter adapter;
    private List<Tour> toursList;
    private ToursByCategoryViewModel viewModel;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(ToursByCategoryViewModel.class);

        View view=inflater.inflate(R.layout.fragment_tours_by_category,container,false);
        recyclerView=view.findViewById(R.id.tours_by_category_fr_rv);
        progressBar=view.findViewById(R.id.tours_by_category_pb);
        toursList=new ArrayList<>();
        adapter=new ToursRecyclerViewAdapter();
        adapter.setOnToursViewHolderCLickListener(this);

        progressBar.setVisibility(View.VISIBLE);
        viewModel.getToursList("lav tour").observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(List<Tour> tours) {
              toursList.addAll(tours);
              adapter.setTours(toursList);
              progressBar.setVisibility(View.GONE);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onToursViewHolderClick(int position) {
        Tour tour=adapter.getTour(position);
        Intent intent = new Intent(getActivity(), ChooseATravelPackageAdd.class);
        intent.putExtra("id",tour.getId());
        intent.putExtra("placeName",tour.getPlaceName());
        intent.putExtra("price",tour.getPrice());
        startActivity(intent);
    }
}
