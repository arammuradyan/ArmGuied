package com.ArmGuide.tourapplication.ui.companies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryChooseATravelPackageAadd;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryViewModel;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TourCompaniesFragment extends Fragment implements AllCompaniesRecyclerViewAdapter.OnToursViewHolderCLickListener {

    private RecyclerView recyclerView;
    private AllCompaniesRecyclerViewAdapter adapter;
    private List<Company> companiesList;
    private ProgressBar progressBar;
    private TextView allCompanies_tv;

    private TourCompaniesViewModel tourCompaniesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        tourCompaniesViewModel = ViewModelProviders.of(this).get(TourCompaniesViewModel.class);

        View root = inflater.inflate(R.layout.fragment_all_companies, container, false);

        progressBar=root.findViewById(R.id.all_companies_pb);
        allCompanies_tv=root.findViewById(R.id.all_companies_tv);

        recyclerView=root.findViewById(R.id.all_companies_fr_rv);
        companiesList=new ArrayList<>();
        adapter=new AllCompaniesRecyclerViewAdapter();

        adapter.setOnToursViewHolderCLickListener(this);

        progressBar.setVisibility(View.VISIBLE);

        tourCompaniesViewModel.getAllCompaniesList().observe(this, new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companies) {
                companiesList.addAll(companies);
                  adapter.setCompanies(companiesList);
                progressBar.setVisibility(View.GONE);
                if(companiesList.size()==0){
                    allCompanies_tv.setVisibility(View.VISIBLE);
                    allCompanies_tv.setText("There are no companies");
                }else{
                    allCompanies_tv.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return root;
    }



    @Override
    public void onToursViewHolderClick(int position) {
    Company company =adapter.getCompany(position);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, new ChoosenCompaniesToursFragment(company))
                    .addToBackStack(null)
                    .commit();}
    }
}