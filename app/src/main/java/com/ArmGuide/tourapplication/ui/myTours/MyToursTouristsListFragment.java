package com.ArmGuide.tourapplication.ui.myTours;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tourist;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class MyToursTouristsListFragment extends Fragment implements MyToursTouristsListRecyclerViewAdapter.OnImageButtonsCLickListener {


    private MyToursViewModel myToursViewModel;
    private MyToursTouristsListRecyclerViewAdapter adapter;
    private List<Tourist> touristsList;

   // views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView noTours_tv;

    private String tourId;

    private final int CALL_PERMISION_RQUEST_CODE=891;
    int positionForPermission;


    public MyToursTouristsListFragment(String tourId) {
    this.tourId=tourId;
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_tours_tourists_list, container, false);

        // view init
        progressBar=root.findViewById(R.id.my_tours_tourists_list_pb);
        recyclerView=root.findViewById(R.id.my_tours_tourists_list_rv);
        noTours_tv=root.findViewById(R.id.my_tours_tourists_list_tv);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));

        touristsList=new ArrayList<>();

        adapter=new MyToursTouristsListRecyclerViewAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnImageButtonsCLickListener(this);


        myToursViewModel = ViewModelProviders.of(this).get(MyToursViewModel.class);

        progressBar.setVisibility(VISIBLE);

        myToursViewModel.getTouristsList(tourId).observe(this, new Observer<List<Tourist>>() {
            @Override
            public void onChanged(List<Tourist> tourists) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"touristlist fragment : onChanged"
                        +tourists.size()+tourId,Toast.LENGTH_SHORT).show();
                if(tourists.size()==0){
                 noTours_tv.setVisibility(VISIBLE);
                 }else{
                touristsList.addAll(tourists);
                adapter.setTourists(touristsList);
                noTours_tv.setVisibility(View.GONE);

                 }

            }
        });


        return root;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onImageButtonsClick(int position, int id) {
        switch (id){
            case R.id.call_btn:{
                positionForPermission=position;
                makeCall(position);
                break;
            }
            case R.id.send_email_btn:{
                sendEmail(position);
                break;
            }
        }
    }

    private void makeCall(int position){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CALL_PHONE},CALL_PERMISION_RQUEST_CODE);
        }else{
            startCallActivity(position);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISION_RQUEST_CODE) {

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CALL_PHONE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    startCallActivity(positionForPermission);
                }
            }
        }

    }
    private void startCallActivity(int position){
        Tourist tourist=adapter.getTourist(position);
        Intent intent=new Intent(Intent.ACTION_CALL);
        String uri= "tel:"+tourist.getPhoneNumber();
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
    private void sendEmail(int position){
        Tourist tourist=adapter.getTourist(position);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+tourist.getEmail())); // only email apps should handle this

        if(getActivity()!=null){
            try {
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }catch(ActivityNotFoundException e) {
                Toast.makeText(getActivity(),"No application found on this device" +
                        " \n to perform send email masage action",Toast.LENGTH_SHORT).show();
            }

        }
    }
}