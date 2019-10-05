package com.ArmGuide.tourapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tourist;
import com.ArmGuide.tourapplication.ui.gallery.GalleryFragment;
import com.ArmGuide.tourapplication.ui.home.HomeFragment;
import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
import com.ArmGuide.tourapplication.ui.slideshow.SlideshowFragment;
import com.ArmGuide.tourapplication.ui.tools.ToolsFragment;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private boolean TOUR_AGENCY=true;
    private boolean locationPermissionsGrabted=false;// karoxa petq ga
    private static final String TAG="MainActivity";
    private static final int PERMISSION_REQUEST_COD=546;
    private static final int ERROR_DIALOG_REQUEST=9001;

    private DrawerLayout drawer;

    private FirebaseAuth mAuth;
    private String userUid="";
    private DatabaseReference touristDatabaseReference;
    private DatabaseReference companyDatabaseReference;
    private ValueEventListener touristValueListener;
    private ValueEventListener companyValueListener;

    // Views on header
    private ImageView avatar_img;
    private TextView name_tv, email_tv;
    private  NavigationView navigationView;
    //FAB
    private FloatingActionButton fab;

   // private Tourist tourist=new Tourist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFireBase();
        initViews(savedInstanceState);
       getLocationPermission();
        isServicesOK();
    }

    @SuppressLint("RestrictedApi")
    private void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Togle color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorBlack));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Status bar color
        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

        if (savedInstanceState == null) {
            // ARAJIN@ BACVOX FRAGMENT
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToursByCategoryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        avatar_img=hView.findViewById(R.id.header_avatar_img);
        name_tv= hView.findViewById(R.id.header_username_tv);
        email_tv=hView.findViewById(R.id.header_email_tv);

        fab=findViewById(R.id.fab);

        fab.setVisibility(View.GONE);
        if(mAuth.getCurrentUser()==null){
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initFireBase(){
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
        userUid=mAuth.getCurrentUser().getUid();
        }
        touristDatabaseReference=FirebaseDatabase.getInstance().getReference(Constants.TOURISTS_DATABASE_REFERENCE);
        companyDatabaseReference=FirebaseDatabase.getInstance().getReference(Constants.COMPANIES_DATABASE_REFERENCE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            userUid=mAuth.getCurrentUser().getUid();
        }
        if(!userUid.isEmpty()){

        Query touristQueryByUid=touristDatabaseReference
                .orderByChild("id")
                .equalTo(userUid);

        touristValueListener=touristQueryByUid.addValueEventListener(new ValueEventListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                Tourist touristfromDB=dataSnapshot.getChildren().iterator().next().getValue(Tourist.class);
                    if(touristfromDB!=null){
                     Toast.makeText(MainActivity.this, touristfromDB.toString(), Toast.LENGTH_LONG).show();

                        TOUR_AGENCY=touristfromDB.getIsCompany();
                 name_tv.setText(touristfromDB.getFullName());
                 email_tv.setText(touristfromDB.getEmail());
                   Picasso.get().load(touristfromDB.getAvatarUrl())
                           .placeholder(R.mipmap.ic_launcher)
                             .fit()
                             .centerCrop()
                             .into(avatar_img);

                        if(!TOUR_AGENCY){
                            fab.setVisibility(View.GONE);
                        }
                   reloadMenu(TOUR_AGENCY);

                    }else{
                     Toast.makeText(MainActivity.this, "Tourist onDataChange TOURIST@ NULLA", Toast.LENGTH_SHORT).show();
                 }
                }
                Toast.makeText(MainActivity.this, "Tourist onDataChange", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Tourist onCancelled: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Query companyQueryByUid=companyDatabaseReference
                    .orderByChild("id")
                    .equalTo(userUid);
            companyValueListener=companyQueryByUid.addValueEventListener(new ValueEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                            Company company=dataSnapshot.getChildren().iterator().next().getValue(Company.class);
                            if(company!=null){
                                TOUR_AGENCY=company.isCompany();

                                if(TOUR_AGENCY){
                                    fab.setVisibility(View.VISIBLE);
                                }
                                reloadMenu(TOUR_AGENCY);

                                name_tv.setText(company.getCompanyName());
                                email_tv.setText(company.getEmail());
                                Picasso.get().load(company.getAvatarUrl())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .fit()
                                        .centerCrop()
                                        .into(avatar_img);
                            }else{
                                Toast.makeText(MainActivity.this, "Company onDataChange COMPANIN NULLA", Toast.LENGTH_SHORT).show();
                            }
                        }
                    Toast.makeText(MainActivity.this, "Company onDataChange", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Company onCancelled: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!userUid.isEmpty()){
        companyDatabaseReference.removeEventListener(companyValueListener);
        touristDatabaseReference.removeEventListener(touristValueListener);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mAuth.getCurrentUser()==null){
            getMenuInflater().inflate(R.menu.main_defoult, menu);
            Toast.makeText(this, "inflating defolt menu", Toast.LENGTH_SHORT).show();

        }
        else if(mAuth.getCurrentUser()!=null && TOUR_AGENCY){
            getMenuInflater().inflate(R.menu.main_tour_agency, menu);
            Toast.makeText(this, "inflating company menu", Toast.LENGTH_SHORT).show();

        }
        else if(mAuth.getCurrentUser()!=null && !TOUR_AGENCY){
            getMenuInflater().inflate(R.menu.main, menu);
           // menu.removeItem(R.id.action_add_new_tour);
            Toast.makeText(this, "inflating tourist menu", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void reloadMenu(boolean TOUR_AGENCY) {
         // Tourist sign in
        if(!TOUR_AGENCY){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        }
        //Tour company sign in
        if(TOUR_AGENCY){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_tour_agency_drawer);
        }
        invalidateOptionsMenu();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(mAuth.getCurrentUser()==null){

            // NO USER defoult menu
            switch (item.getItemId()){
//                case R.id.action_add_new_tour:
//                    Toast.makeText(this, "defoult menu", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.action_settings:
                    Toast.makeText(this, "defoult menu", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_sign_in:
                    if(mAuth.getCurrentUser()==null){
                        Intent intent =new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "defolt sign in item", Toast.LENGTH_SHORT).show();}
                    break;
            }
        }
        else if(TOUR_AGENCY){

            // COMPANI items
            switch (item.getItemId()){
//                case R.id.action_add_new_tour:
//                    Toast.makeText(this, "company add item", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.action_settings:
                    Toast.makeText(this, "company setings item", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.action_sign_out:
                    signOut();
                    Toast.makeText(this, "company sign out", Toast.LENGTH_SHORT).show();
                    break;
            }
            }else {
            // TOURIST menu items
            switch (item.getItemId()){
                case R.id.action_settings:
                    Toast.makeText(this, "tourist setings item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_sign_out:
                   signOut();
                    Toast.makeText(this, "tourist sign out", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       if(mAuth.getCurrentUser()==null){
          // DEFOULT items
           switch (menuItem.getItemId()) {
               case R.id.nav_home:
                   //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           new ToursByCategoryFragment()).commit();
                   break;
               case R.id.nav_gallery:
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           new GalleryFragment()).addToBackStack("mainStack").commit();
                   break;
               case R.id.nav_slideshow:
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           new SlideshowFragment()).addToBackStack("mainStack").commit();
                   break;
               case R.id.nav_tools:
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           new ToolsFragment()).addToBackStack("mainStack").commit();
                   break;
               case R.id.nav_share:
               {//CURENT location
                   getLocationPermission();
                   if(locationPermissionsGrabted){
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment(
                           locationPermissionsGrabted)).commit();
                   }
                   else{
                       Toast.makeText(this, " Cant show curent location permissions denied", Toast.LENGTH_SHORT).show();
                   }

                   Toast.makeText(this, " defoult share item", Toast.LENGTH_SHORT).show();
                   break;
               }
               case R.id.nav_send:
                   //COOSEN PLACE
                   getLocationPermission();

                   getSupportFragmentManager()
                           .beginTransaction()
                           .replace(R.id.fragment_container,
                                   new MapFragment(locationPermissionsGrabted,
                                           PlaceInfoRepository.ZOOM_CITY,
                                           PlaceInfoRepository.getPlaceInfo(PlaceInfoRepository.CAXKADZOR)))
                           .commit();
                   Toast.makeText(this, "defoult send item", Toast.LENGTH_SHORT).show();
                   break;
           }
       }else if(TOUR_AGENCY) {
            // COMPANY items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ToursByCategoryFragment()).commit();
                    break;
                case R.id.nav_gallery:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new GalleryFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_slideshow:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SlideshowFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ToolsFragment()).addToBackStack("mainStack").commit();
                    break;
                case R.id.nav_share:
                {//CURENT location
                    getLocationPermission();
                    if(locationPermissionsGrabted){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment(
                                locationPermissionsGrabted)).commit();
                    }
                    else{
                        Toast.makeText(this, " Cant show curent location permissions denied", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(this, " COMPANY share item", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.nav_send:
                    //COOSEN PLACE
                    getLocationPermission();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                            new MapFragment(locationPermissionsGrabted,
                                    PlaceInfoRepository.ZOOM_CITY,
                                    PlaceInfoRepository.getPlaceInfo(PlaceInfoRepository.CAXKADZOR)))
                                    .commit();
                    Toast.makeText(this, "COMPANY send item", Toast.LENGTH_SHORT).show();
                    break;
            }
       } else{
            // TOURIST menu items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                    if(getSupportActionBar()!=null)
                    getSupportActionBar().setTitle("Tour diractions");
                    break;
                case R.id.nav_gallery:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new GalleryFragment()).commit();
                    if(getSupportActionBar()!=null)
                        getSupportActionBar().setTitle("Tour companies");
                    break;
                case R.id.nav_slideshow:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SlideshowFragment()).commit();
                    if(getSupportActionBar()!=null)
                        getSupportActionBar().setTitle("All tours");

                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ToolsFragment()).commit();
                    if(getSupportActionBar()!=null)
                        getSupportActionBar().setTitle("My tours");

                    break;
                case R.id.nav_share:
                    getLocationPermission();
                    //COOSEN PLACE
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    new MapFragment(locationPermissionsGrabted,
                                            PlaceInfoRepository.ZOOM_COUTRY,
                                            PlaceInfoRepository.getPlaceInfo(PlaceInfoRepository.ARMENIA)))
                            .commit();
                    if(getSupportActionBar()!=null)
                        getSupportActionBar().setTitle("Map of Armenia");

                    Toast.makeText(this, "TOURIST share item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_send:
                    //CURENT location
                    getLocationPermission();
                    if(locationPermissionsGrabted){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment(
                                locationPermissionsGrabted)).commit();
                    }
                    else{
                        Toast.makeText(this, " Cant show curent location permissions denied", Toast.LENGTH_SHORT).show();

                    }
                    if(getSupportActionBar()!=null)
                        getSupportActionBar().setTitle("Curent location");
                    Toast.makeText(this, "TOURIST send item", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("RestrictedApi")
    public void signOut(){
        if(mAuth.getCurrentUser()!=null){
            mAuth.signOut();

        avatar_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_avatar));
        name_tv.setText("Name");
        email_tv.setText("Email");
        TOUR_AGENCY=true;
        userUid="";
        fab.setVisibility(View.VISIBLE);

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        invalidateOptionsMenu();
        }
    }

    /*
    * ------------------------------MAP PERMISSIONS END GOOGLE MAP SERVICE CHECK----------------------------------------------------
    * */
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
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

