package com.ArmGuide.tourapplication.ui.myTours;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Tour;
import com.squareup.picasso.Picasso;


public class MyToursEditTour extends Fragment {


    private ImageView image,companyImage;
    private TextView companiInfo_tv;
    private TextView thePackageYouSelected;
    private TextView tourData_TV;
    private TextView price_TV;
    private CheckBox includingTransport_CB;
    private CheckBox indudingFood_CB;
    private CheckBox threeLanguageGuiding_CB;
    private CheckBox vineDegustation_CB;
    private CheckBox freeWifiDuringTour_CB;
    private TextView title_moreInfo_TV;
    private TextView moreInformation_TV;

    private LinearLayout touristLay;
    private ImageButton make_call_btn;
    private ImageButton send_email_btn;
    private ImageButton website_btn;

    private LinearLayout companyLay;
    private LinearLayout companyInfoLay;
    private ImageButton opeen_tourists_list_btn;

    private final int CALL_PERMISION_RQUEST_CODE=89;

    private Tour tour;

    public MyToursEditTour(Tour tour) {
        this.tour = tour;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tours_tour_edit, container, false);
        viewInit(view);
        setTourInformation();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if(TOUR_AGENCY){
//            companyLay.setVisibility(View.VISIBLE);
//
//        }else{
//            touristLay.setVisibility(View.VISIBLE);
//            companyInfoLay.setVisibility(View.VISIBLE);
//
//        }

        make_call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             makeCall();
            }
        });

        send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailMasage();
            }
        });

        website_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCompanyWebPage();
            }
        });



        opeen_tourists_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new MyToursTouristsListFragment(tour.getId())).addToBackStack(null).commit();
                }
            }
        });

    }

    private void viewInit(View view){

    touristLay=view.findViewById(R.id.tourist_buttons_linlay);
    make_call_btn=view.findViewById(R.id.tourist_make_call_btn);
    send_email_btn=view.findViewById(R.id.tourist_send_email_btn);
    website_btn=view.findViewById(R.id.tourist_goto_web_btn);

    companyLay=view.findViewById(R.id.company_button_linlay);
    companyInfoLay=view.findViewById(R.id.company_info_linlay);
    opeen_tourists_list_btn=view.findViewById(R.id.company_open_tourists_list_btn);



    companyImage=view.findViewById(R.id.image2);
    image = view.findViewById(R.id.image);

    companiInfo_tv=view.findViewById(R.id.company_name);

    thePackageYouSelected = view.findViewById(R.id.thePackageYouSelected);
    tourData_TV = view.findViewById(R.id.tourData_TV);
    price_TV = view.findViewById(R.id.price_TV);

    includingTransport_CB = view.findViewById(R.id.includingTransport_CB);
    indudingFood_CB = view.findViewById(R.id.indudingFood_CB);
    threeLanguageGuiding_CB = view.findViewById(R.id.threeLanguageGuiding_CB);
    vineDegustation_CB = view.findViewById(R.id.vineDegustation_CB);
    freeWifiDuringTour_CB = view.findViewById(R.id.freeWifiDuringTour_CB);

    title_moreInfo_TV = view.findViewById(R.id.title_moreInfo_TV);
    moreInformation_TV = view.findViewById(R.id.moreInformation_TV);
}

private void setTourInformation(){
   if(tour.getTourCompany().getAvatarUrl()!=null){
        if(!tour.getTourCompany().getAvatarUrl().isEmpty())
        { Picasso.get().load(tour.getTourCompany().getAvatarUrl())
                .placeholder(R.drawable.ic_avatar)
                .fit()
                .centerCrop()
                .into(companyImage);
        }
    }

        if(tour.getImgUrl()!=null){
        if(!tour.getImgUrl().isEmpty())
        { Picasso.get().load(tour.getImgUrl())
                .placeholder(R.drawable.ic_avatar)
                .fit()
                .centerCrop()
                .into(image);}
        }
String companyInfo=tour.getTourCompany().getCompanyName()+"\n"
        +tour.getTourCompany().getPhoneNumber()+"\n"
                   +tour.getTourCompany().getAddress()+"\n"
                   +tour.getTourCompany().getEmail()+"\n"
                   +tour.getTourCompany().getWebUrl();
        companiInfo_tv.setText(companyInfo);

    thePackageYouSelected.setText(tour.getPlaceName());
    tourData_TV.setText(tour.getDate());
    price_TV.setText(String.valueOf(tour.getPrice()));

    if(tour.getMoreInfo().equals("COMPANY HAS DELETED THIS TOUR")){
        moreInformation_TV.setTextColor( moreInformation_TV.getContext().getResources().getColor(R.color.colorRed));
        moreInformation_TV.setText(tour.getMoreInfo());
    }else{
        moreInformation_TV.setText(tour.getDate());}


   // moreInformation_TV.setText(tour.getMoreInfo());




    includingTransport_CB.setChecked(tour.isTransport());
    indudingFood_CB.setChecked(tour.isFood());
    threeLanguageGuiding_CB.setChecked(tour.isThreeLangGuide());
    vineDegustation_CB.setChecked(tour.isVineDegustation());
    freeWifiDuringTour_CB.setChecked(tour.isWifi());

    includingTransport_CB.setClickable(false);
    indudingFood_CB.setClickable(false);
    threeLanguageGuiding_CB.setClickable(false);
    vineDegustation_CB.setClickable(false);
    freeWifiDuringTour_CB.setClickable(false);
}
private void makeCall(){
    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CALL_PHONE},CALL_PERMISION_RQUEST_CODE);
    }else{
         startCallActivity();
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISION_RQUEST_CODE) {

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CALL_PHONE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startCallActivity();
                }
            }
        }

    }
    private void startCallActivity(){
        Intent intent=new Intent(Intent.ACTION_CALL);
        String uri= "tel:"+tour.getTourCompany().getPhoneNumber();
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
    private void sendEmailMasage(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+tour.getTourCompany().getEmail())); // only email apps should handle this
        //intent.putExtra(Intent.EXTRA_EMAIL, new String[]{tour.getTourCompany().getEmail()});
       // intent.putExtra(Intent.EXTRA_SUBJECT, "Write your masage here");
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

    private void openCompanyWebPage() {
        //if(!tour.getTourCompany().getWebUrl().isEmpty()&& tour.getTourCompany().getWebUrl()!=null){
        Intent intentWeb = new Intent(getActivity(), WebActivity.class);
        intentWeb.putExtra("uri", "http://anitour.am/");
        startActivity(intentWeb);
       // }else{
          //  Toast.makeText(getActivity(),"Company doesn't have web page",Toast.LENGTH_SHORT).show();
        //}
    }
}









