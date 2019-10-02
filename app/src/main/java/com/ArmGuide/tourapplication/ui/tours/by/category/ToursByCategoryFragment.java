package com.ArmGuide.tourapplication.ui.tours.by.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class ToursByCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToursRecyclerViewAdapter adapter;
    private List<String> listOfObjects;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View view=inflater.inflate(R.layout.fragment_tours_by_category,container,false);
        recyclerView=view.findViewById(R.id.tours_by_category_fr_rv);
        listOfObjects=new ArrayList<>();
        adapter=new ToursRecyclerViewAdapter();

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                listOfObjects.add(s);
                adapter.setNames(listOfObjects);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
