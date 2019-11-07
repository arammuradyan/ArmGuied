package com.ArmGuide.tourapplication.ui.companies;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.StateViewModel;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.createTour.CreateTourActivity;
import com.ArmGuide.tourapplication.ui.myTours.MyToursChooseATravelPackageAdd;
import com.ArmGuide.tourapplication.ui.myTours.MyToursRecyclerViewAdapter;
import com.ArmGuide.tourapplication.ui.myTours.MyToursViewModel;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryChooseATravelPackageAadd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class ChoosenCompaniesToursFragment extends Fragment implements ChoosenCompanyToursRecyclerViewAdapter.OnToursViewHolderCLickListener {


  //  private boolean TOUR_AGENCY;
    private TourCompaniesViewModel tourCompaniesViewModel;
   // private StateViewModel stateViewModel;
    private ChoosenCompanyToursRecyclerViewAdapter adapter;
    private List<Tour> toursList;

   // views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView noTours_tv,company_name_tv;
    private ImageView company_avatar_img;

    private Company company;


    public ChoosenCompaniesToursFragment(/*boolean TOUR_AGENCY,*/Company company) {
      //  this.TOUR_AGENCY = TOUR_AGENCY;
        this.company=company;
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_tours_coordinator_layout, container, false);

        // view init
        progressBar=root.findViewById(R.id.my_tours_pb);
        recyclerView=root.findViewById(R.id.my_tours_rv);
        noTours_tv=root.findViewById(R.id.my_tours_tv);

        company_avatar_img=root.findViewById(R.id.company_img_colay);
        company_name_tv=root.findViewById(R.id.company_name_tv_colay);

        company_name_tv.setText(company.getCompanyName());

        if(company.getAvatarUrl()!=null){
            if(!company.getAvatarUrl().isEmpty()){
                Picasso.get().load(company.getAvatarUrl())
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(company_avatar_img);}
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        toursList=new ArrayList<>();
        adapter=new ChoosenCompanyToursRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnToursViewHolderCLickListener(this);


        tourCompaniesViewModel = ViewModelProviders.of(this).get(TourCompaniesViewModel.class);

       // if(getActivity()!=null)
       // stateViewModel = ViewModelProviders.of(getActivity()).get(StateViewModel.class);

        getCompanyTours();


        return root;
    }

    private void getCompanyTours(){
        progressBar.setVisibility(View.VISIBLE);

        tourCompaniesViewModel.getCompanyToursList(company).observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(List<Tour> tours) {
                progressBar.setVisibility(View.GONE);
                if (tours.size() == 0) {
                    noTours_tv.setVisibility(VISIBLE);
                } else {
                    noTours_tv.setVisibility(View.GONE);
                    toursList.clear();
                    toursList.addAll(tours);
                    adapter.setTours(toursList);
                }
            }
        });
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

    @Override
    public void onEditButtonClick(int position) {
    }
}


