package com.ArmGuide.tourapplication.ui.allTours;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.StateViewModel;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.companies.AllCompaniesRecyclerViewAdapter;
import com.ArmGuide.tourapplication.ui.myTours.MyToursChooseATravelPackageAdd;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryChooseATravelPackageAadd;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class AllToursFragment extends Fragment implements AllToursRecyclerViewAdapter.OnToursViewHolderCLickListener {

    private RecyclerView recyclerView;
    private AllToursRecyclerViewAdapter adapter;
    private List<Tour> allTourList;
    private ProgressBar progressBar;
    private TextView allTours_tv;

    private StateViewModel stateViewModel;
    private AllToursViewModel allToursViewModel;
    private boolean TOUR_AGENCY;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_tours, container, false);

        allToursViewModel= ViewModelProviders.of(this).get(AllToursViewModel.class);

        if(getActivity()!=null)
            stateViewModel = ViewModelProviders.of(getActivity()).get(StateViewModel.class);

        progressBar=root.findViewById(R.id.all_tours_pb);
        allTours_tv=root.findViewById(R.id.all_tours_tv);

        recyclerView=root.findViewById(R.id.all_tours_fr_rv);
        allTourList=new ArrayList<>();
        adapter=new AllToursRecyclerViewAdapter();

        adapter.setOnToursViewHolderCLickListener(this);

        progressBar.setVisibility(View.VISIBLE);
        allToursViewModel.getAllToursList().observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(List<Tour> allTours) {
                allTourList.addAll(allTours);
                adapter.setTours(allTourList);
                progressBar.setVisibility(View.GONE);
                if(allTourList.size()==0){
                    allTours_tv.setVisibility(View.VISIBLE);
                    allTours_tv.setText("There are no tours");
                }else{
                    allTours_tv.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);


        if(getActivity()!=null&& FirebaseAuth.getInstance().getCurrentUser()!=null)
            stateViewModel.getState().observe(this, new Observer<Boolean>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean ){
                        TOUR_AGENCY=aBoolean;
                    }else{
                        TOUR_AGENCY=aBoolean;
                    }
                }
            });




        return root;
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