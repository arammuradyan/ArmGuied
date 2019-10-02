package com.ArmGuide.tourapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import com.ArmGuide.tourapplication.ui.gallery.GalleryFragment;
import com.ArmGuide.tourapplication.ui.home.HomeFragment;
import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.slideshow.SlideshowFragment;
import com.ArmGuide.tourapplication.ui.tools.ToolsFragment;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private boolean TOUR_AGENCY=true;
    private boolean locationPermissionsGrabted=false;// karoxa petq ga
    private static final String TAG="MainActivity";
    private static final int PERMISSION_REQUEST_COD=546;
    private static final int ERROR_DIALOG_REQUEST=9001;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CHEK ENQ ANUM ETE getCurentUser()!=null EV TOUR AGENT E -> TOUR_AGENCY=true else false;

        getLocationPermission();
        isServicesOK();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(TOUR_AGENCY){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_tour_agency_drawer);
        }

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // ARAJIN@ BACVOX FRAGMENT
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToursByCategoryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(TOUR_AGENCY){
            getMenuInflater().inflate(R.menu.main_tour_agency, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(TOUR_AGENCY){
            // tour agency items
            switch (item.getItemId()){
                case R.id.action_add_new_tour:
                    Toast.makeText(this, "tour agency add item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_settings:
                    Toast.makeText(this, "tour agency setings item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_log_out:
                    Toast.makeText(this, "tour agency log out item", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else{
            // user menu items
                switch (item.getItemId()){
                    case R.id.action_settings:
                        Toast.makeText(this, "user setings item", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_log_out:
                        Toast.makeText(this, "user log out item", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(TOUR_AGENCY) {
            // tour agency items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToursByCategoryFragment()).commit();
                    break;
                case R.id.nav_gallery:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_slideshow:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SlideshowFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToolsFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_share:
                {
                    //CURENT location
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment(
                            locationPermissionsGrabted)).commit();

                    Toast.makeText(this, "share item", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.nav_send:
                    //COOSEN PLACE
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                            new MapFragment(locationPermissionsGrabted,
                                    PlaceInfoRepository.ZOOM_CITY,
                                    PlaceInfoRepository.getPlaceInfo(PlaceInfoRepository.CAXKADZOR)))
                                    .commit();
                    Toast.makeText(this, "send item", Toast.LENGTH_SHORT).show();
                    break;
            }


       } else{
            // user menu items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
                case R.id.nav_gallery:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
                    break;
                case R.id.nav_slideshow:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SlideshowFragment()).commit();
                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToolsFragment()).commit();
                    break;
                case R.id.nav_share:
                    Toast.makeText(this, "share item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_send:
                    Toast.makeText(this, "send item", Toast.LENGTH_SHORT).show();
                    break;
            }
       }
       drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();}
    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServiceOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available== ConnectionResult.SUCCESS){
            // evriting ok can use map
            Log.d(TAG,"isServiceOk: Googgle play service working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // ann eror that we can resolve it
            Log.d(TAG,"isServiceOk: ann eror but we can solve it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"You cant use maps",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission(){
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
                  locationPermissionsGrabted=true;
            }else{
                ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_COD);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_COD);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionsGrabted=false;
        switch(requestCode){
            case PERMISSION_REQUEST_COD:{
                if(grantResults.length>0){
                    for (int i = 0; i <grantResults.length ; i++) {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            locationPermissionsGrabted=false;
                            return;
                        }
                    }
                    locationPermissionsGrabted=true;
                    // inital map
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

