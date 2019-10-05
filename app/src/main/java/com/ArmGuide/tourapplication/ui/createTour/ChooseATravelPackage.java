package com.ArmGuide.tourapplication.ui.createTour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.ArmGuide.tourapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseATravelPackage extends AppCompatActivity {

    private CircleImageView image;
    private TextView selectTheDirection;
    private Spinner touristDestinations;
    private TextView title_tourData_TV;
    private TextView tourData_TV;
    private TextView title_price_TV;
    private TextView price_TV;
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
    private TextView title_moreInfo_TV;
    private TextView moreInformation_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_atravel_package);



        image=findViewById(R.id.image);
        selectTheDirection=findViewById(R.id.selectTheDirection);
        touristDestinations=findViewById(R.id.touristDestinations);
        title_tourData_TV=findViewById(R.id.title_tourData_TV);
        tourData_TV=findViewById(R.id.tourData_TV);
        title_price_TV=findViewById(R.id.title_price_TV);
        price_TV=findViewById(R.id.price_TV);
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
        title_moreInfo_TV=findViewById(R.id.title_moreInfo_TV);
        moreInformation_TV=findViewById(R.id.moreInformation_ET);
    }
}
