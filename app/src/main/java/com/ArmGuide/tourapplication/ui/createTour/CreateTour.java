package com.ArmGuide.tourapplication.ui.createTour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ArmGuide.tourapplication.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateTour extends AppCompatActivity {

    private CircleImageView image;
    private TextView selectTheDirection;
    private Spinner tourPackageSpinner;
    private TextView tourData_TV;
    private EditText tourData_ET;
    private TextView price_TV;
    private EditText price_ET;
    private TextView dram;
    private TextView includingTransport_TV;
    private TextView indudingFood_TV;
    private TextView threeLanguageGuiding_TV;
    private TextView vineDegustation_TV;
    private TextView freeWifiDuringTour_TV;
    private CheckBox includingTransport_CB;
    private CheckBox indudingFood_CB;
    private CheckBox threeLanguageGuiding_CB;
    private CheckBox vineDegustation_CB;
    private CheckBox freeWifiDuringTour_CB;
    private TextView moreInformation_TV;
    private EditText moreInformation_ET;
    private TourPackageSpinerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        image=findViewById(R.id.image);
        selectTheDirection=findViewById(R.id.selectTheDirection);
        tourPackageSpinner =findViewById(R.id.touristDestinations);
        tourData_TV=findViewById(R.id.tourData_TV);
        tourData_ET=findViewById(R.id.tourData_ET);
        price_TV=findViewById(R.id.price_TV);
        price_ET=findViewById(R.id.price_ET);
        dram=findViewById(R.id.dram);
        includingTransport_TV=findViewById(R.id.includingTransport_TV);
        indudingFood_TV=findViewById(R.id.indudingFood_TV);
        threeLanguageGuiding_TV=findViewById(R.id.threeLanguageGuiding_TV);
        vineDegustation_TV=findViewById(R.id.vineDegustation_TV);
        freeWifiDuringTour_TV=findViewById(R.id.freeWifiDuringTour_TV);
        includingTransport_CB=findViewById(R.id.includingTransport_CB);
        indudingFood_CB=findViewById(R.id.indudingFood_CB);
        threeLanguageGuiding_CB=findViewById(R.id.threeLanguageGuiding_CB);
        vineDegustation_CB=findViewById(R.id.vineDegustation_CB);
        freeWifiDuringTour_CB=findViewById(R.id.freeWifiDuringTour_CB);
        moreInformation_TV=findViewById(R.id.moreInformation_TV);
        moreInformation_ET=findViewById(R.id.moreInformation_ET);

        adapter = new TourPackageSpinerAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tourPackageSpinner.setAdapter(adapter);

        List<String> packages = new ArrayList<>();

  /*      packages.add("asd");
        packages.add("asd");
        packages.add("asd");
        packages.add("asd");
        packages.add("asd");
*/
        adapter.addTourPackages(packages);
    }


}
