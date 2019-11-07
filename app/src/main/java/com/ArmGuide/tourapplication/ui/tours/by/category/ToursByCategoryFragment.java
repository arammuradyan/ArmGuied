package com.ArmGuide.tourapplication.ui.tours.by.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.StateViewModel;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.createTour.ChooseATravelPackageAdd;
import com.ArmGuide.tourapplication.ui.createTour.CreateTourActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ToursByCategoryFragment extends Fragment implements ToursRecyclerViewAdapter.OnToursViewHolderCLickListener {

    private RecyclerView recyclerView;
    private ToursRecyclerViewAdapter adapter;
    private List<Tour> toursList;
    private ProgressBar progressBar;
    private TextView noTours_tv,place_name_tv;
    private ImageView place_img;
    private Place place;

    private ToursByCategoryViewModel viewModel;



    public ToursByCategoryFragment(Place place) {
        this.place = place;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(ToursByCategoryViewModel.class);

        View view=inflater.inflate(R.layout.fragment_tours_by_category_coordinator_layout,container,false);

        place_name_tv=view.findViewById(R.id.place_name_tv_colay);
        place_name_tv.setText(place.getName());

        place_img=view.findViewById(R.id.place_img_colay);
        if(place.getImageUrls().get(0)!=null){
            if(!place.getImageUrls().get(0).isEmpty()){
                Picasso.get().load(place.getImageUrls().get(0))
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(place_img);}
        }

        progressBar=view.findViewById(R.id.tours_by_category_pb);
        noTours_tv=view.findViewById(R.id.tours_by_category_tv);

        recyclerView=view.findViewById(R.id.tours_by_category_fr_rv);
        toursList=new ArrayList<>();
        adapter=new ToursRecyclerViewAdapter();
        adapter.setOnToursViewHolderCLickListener(this);

        progressBar.setVisibility(View.VISIBLE);
        viewModel.getToursList(place.getName()).observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(List<Tour> tours) {
              toursList.addAll(tours);
              adapter.setTours(toursList);
              progressBar.setVisibility(View.GONE);
              if(toursList.size()==0){
                  noTours_tv.setVisibility(View.VISIBLE);
              }else{
                  noTours_tv.setVisibility(View.GONE);
              }

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onToursViewHolderClick(int position) {
        Tour tour=adapter.getTour(position);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, new ToursByCategoryChooseATravelPackageAadd(tour))
                    .addToBackStack(null)
                    .commit();}
    }


}
