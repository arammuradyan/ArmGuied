package com.ArmGuide.tourapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.Repositories.RepositoryForUserState;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tourist;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.allTours.AllToursFragment;
import com.ArmGuide.tourapplication.ui.companies.TourCompaniesFragment;
import com.ArmGuide.tourapplication.ui.home.HomeFragment;
import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.myTours.MyToursFragment;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean TOUR_AGENCY = true;
    private boolean locationPermissionsGrabted = false;// karoxa petq ga
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_COD = 546;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private DrawerLayout drawer;

    private FirebaseAuth mAuth;
    private String userUid = "";
    private DatabaseReference touristDatabaseReference;
    private DatabaseReference companyDatabaseReference;
    private ValueEventListener touristValueListener;
    private ValueEventListener companyValueListener;

    // Views on header
    private ImageView avatar_img;
    private TextView name_tv, email_tv;
    private NavigationView navigationView;
    //FAB
    private FloatingActionButton fab;
    private RepositoryForUserState repositoryForUserState;

    // view models
   private StateViewModel stateViewModel;
   private Company companyForInfoWindow;
   private Tourist touristForInfoWindow;
    String title="";
    String info="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateViewModel=ViewModelProviders.of(this).get(StateViewModel.class);
        repositoryForUserState = RepositoryForUserState.getInstance();
        getUserState();
        initFireBase();
        initViews(savedInstanceState);
        getLocationPermission();
        isServicesOK();

        stateViewModel.getState().observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && companyForInfoWindow!=null){
                    info=companyForInfoWindow.getCompanyName()+"\n"
                            +companyForInfoWindow.getPhoneNumber()+"\n"
                            +companyForInfoWindow.getAddress()+"\n"
                            +companyForInfoWindow.getEmail()+"\n"
                            +companyForInfoWindow.getWebUrl();
                }else if(touristForInfoWindow!=null){
                    info=touristForInfoWindow.getFullName()+"\n"
                            +touristForInfoWindow.getPhoneNumber()+"\n"
                            +touristForInfoWindow.getEmail()+"\n";
                }
            }
        });

        Log.d("myMain", "onCreate");
    }

    @SuppressLint("RestrictedApi")
    private void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

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
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));

        if (savedInstanceState == null) {
            // ARAJIN@ BACVOX FRAGMENT
           ArrayList<Place> placies=new ArrayList<>();
            Bundle bundle=  getIntent().getBundleExtra("placiesList");
            placies.addAll((ArrayList<Place>)bundle.getSerializable("placiesList"));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new HomeFragment(placies), "home")
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        avatar_img = hView.findViewById(R.id.header_avatar_img);
        name_tv = hView.findViewById(R.id.header_username_tv);
        email_tv = hView.findViewById(R.id.header_email_tv);

        avatar_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(mAuth.getCurrentUser()==null){
                    title="You aren't sign in";
                    info="No info to show";
                    showInfoWindowDialog();
                }else{
                    title="PROFILE INFO";
                    if(companyForInfoWindow!=null || touristForInfoWindow!=null){
                        showInfoWindowDialog();
                    }
                }
                return false;
            }
        });

    }

    private void showInfoWindowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(info)
                .setIcon(R.drawable.ic_application);
        AlertDialog infoWindowDialog = builder.create();
        infoWindowDialog.show();
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userUid = mAuth.getCurrentUser().getUid();
        }
        touristDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.TOURISTS_DATABASE_REFERENCE);
        touristDatabaseReference.keepSynced(true);
        companyDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.COMPANIES_DATABASE_REFERENCE);
        companyDatabaseReference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("myMain", "onStart");

        if (mAuth.getCurrentUser() != null) {
            userUid = mAuth.getCurrentUser().getUid();
        }
        if (!userUid.isEmpty()) {

            Query touristQueryByUid = touristDatabaseReference
                    .orderByChild("id")
                    .equalTo(userUid);

            touristValueListener = touristQueryByUid.addValueEventListener(new ValueEventListener() {

                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Tourist touristfromDB = dataSnapshot.getChildren().iterator().next().getValue(Tourist.class);

                        touristForInfoWindow=touristfromDB;

                        if (touristfromDB != null) {
                            Toast.makeText(MainActivity.this, touristfromDB.toString(), Toast.LENGTH_LONG).show();

                            TOUR_AGENCY = touristfromDB.getIsCompany();

                            //reloadMenu(TOUR_AGENCY);
                           // StateViewModel stateViewModel = ViewModelProviders.of(MainActivity.this).get(StateViewModel.class);

                            stateViewModel.setState(TOUR_AGENCY);

                            name_tv.setText(touristfromDB.getFullName());
                            email_tv.setText(touristfromDB.getEmail());
                            if (!touristfromDB.getAvatarUrl().isEmpty()) {
                                Picasso.get().load(touristfromDB.getAvatarUrl())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .fit()
                                        .centerCrop()
                                        .into(avatar_img);
                            }

                            reloadMenu(TOUR_AGENCY);

                        } else {
                            Toast.makeText(MainActivity.this, "Tourist onDataChange TOURIST@ NULLA", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(MainActivity.this, "Tourist onDataChange", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Tourist onCancelled: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            Query companyQueryByUid = companyDatabaseReference
                    .orderByChild("id")
                    .equalTo(userUid);
            companyValueListener = companyQueryByUid.addValueEventListener(new ValueEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Company company = dataSnapshot.getChildren().iterator().next().getValue(Company.class);

                        companyForInfoWindow=company;

                        if (company != null) {
                            TOUR_AGENCY = company.isCompany();

                           // StateViewModel stateViewModel = ViewModelProviders.of(MainActivity.this).get(StateViewModel.class);
                            stateViewModel.setState(TOUR_AGENCY);
                            reloadMenu(TOUR_AGENCY);

                            name_tv.setText(company.getCompanyName());
                            email_tv.setText(company.getEmail());
                            if (!company.getAvatarUrl().isEmpty()) {
                                Picasso.get().load(company.getAvatarUrl())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .fit()
                                        .centerCrop()
                                        .into(avatar_img);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Company onDataChange COMPANIN NULLA", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(MainActivity.this, "Company onDataChange", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Company onCancelled: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStop() {

        Log.d("myMain", "onStop");

        super.onStop();
        if (!userUid.isEmpty()) {
            companyDatabaseReference.removeEventListener(companyValueListener);
            touristDatabaseReference.removeEventListener(touristValueListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("myMain", "onCreateOptionsMenu");

        if (mAuth.getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.main_defoult, menu);
            Toast.makeText(this, "inflating defolt menu", Toast.LENGTH_SHORT).show();

        } else if (mAuth.getCurrentUser() != null && TOUR_AGENCY) {
            getMenuInflater().inflate(R.menu.main_tour_agency, menu);
            Toast.makeText(this, "inflating company menu", Toast.LENGTH_SHORT).show();

        } else if (mAuth.getCurrentUser() != null && !TOUR_AGENCY) {
            getMenuInflater().inflate(R.menu.main, menu);
            // menu.removeItem(R.id.action_add_new_tour);
            Toast.makeText(this, "inflating tourist menu", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void reloadMenu(boolean TOUR_AGENCY) {
        // Tourist sign in
        if (!TOUR_AGENCY) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            navigationView.setCheckedItem(R.id.nav_home);

        }
        //Tour company sign in
        if (TOUR_AGENCY) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_tour_agency_drawer);
            navigationView.setCheckedItem(R.id.nav_home);

        }
        invalidateOptionsMenu();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("myMain", "onOptionsItemSelected");

        if (mAuth.getCurrentUser() == null) {
            // NO USER defoult menu
            switch (item.getItemId()) {
//                case R.id.action_add_new_tour:
//                    Toast.makeText(this, "defoult menu", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.action_settings:
                    Toast.makeText(this, "defoult menu", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_sign_in:
                    if (mAuth.getCurrentUser() == null) {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "defolt sign in item", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else if (TOUR_AGENCY) {
            // COMPANI items
            switch (item.getItemId()) {
//                case R.id.action_add_new_tour:
//                    Toast.makeText(this, "company add item", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.action_settings:
                    Toast.makeText(this, "company setings item", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.action_sign_out:
                    showSignOutDialog();
                    //signOut();
                    Toast.makeText(this, "company sign out", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            // TOURIST menu items
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Toast.makeText(this, "tourist setings item", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_sign_out:
                    showSignOutDialog();
                    //signOut();
                    Toast.makeText(this, "tourist sign out", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("myMain", "onNavigationItemSelected");

        if (mAuth.getCurrentUser() == null) {
            // DEFOULT items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showHomeFragment();
                    break;
                case R.id.nav_tour_companies:
                    showTourCompaniesFragment();
                    break;
                case R.id.nav_all_tours:
                    showAllToursFragment();
                    break;
                case R.id.nav_my_tours:
                    // fab.setVisibility(View.VISIBLE);
                    showMyToursFragment();
                    break;
                case R.id.nav_map_of_armenia:
                    showMapofArmenia();
                    break;
                case R.id.nav_current_location:
                    showCurrentLocation();
                    break;
            }
        } else if (TOUR_AGENCY) {
            // COMPANY items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showHomeFragment();
                    break;
                case R.id.nav_tour_companies:
                    showTourCompaniesFragment();
                    break;
                case R.id.nav_all_tours:
                    showAllToursFragment();
                    break;
                case R.id.nav_my_tours:
                    showMyToursFragment();
                    // fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_map_of_armenia:
                    showMapofArmenia();
                    break;

                case R.id.nav_current_location:
                    showCurrentLocation();
                    break;
            }
        } else {
            // TOURIST menu items
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showHomeFragment();
                    break;
                case R.id.nav_tour_companies:
                    showTourCompaniesFragment();
                    break;
                case R.id.nav_all_tours:
                    showAllToursFragment();
                    break;
                case R.id.nav_my_tours:
                    showMyToursFragment();
                    break;
                case R.id.nav_map_of_armenia:
                    showMapofArmenia();
                    break;
                case R.id.nav_current_location:
                    showCurrentLocation();
                    break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("myMain", "onBackPressed");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            Toast.makeText(MainActivity.this, "arajin else", Toast.LENGTH_SHORT).show();

        }/* else if( navigationView.getCheckedItem().getItemId()!=R.id.nav_home){
            navigationView.setCheckedItem(R.id.nav_home);
            showHomeFragment();
        }*/ else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportActionBar().setTitle("Tour diractions");
                Toast.makeText(MainActivity.this, "superi if ", Toast.LENGTH_SHORT).show();
            }
            super.onBackPressed();
        }
    }

    private void showHomeFragment() {
        getSupportFragmentManager().popBackStack();
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                //  .addToBackStack(null)
                .commit();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Tour diractions");
    }

    private void showTourCompaniesFragment() {
        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new TourCompaniesFragment())
                .addToBackStack(null)
                .commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Tour companies");
    }

    private void showAllToursFragment() {
        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new AllToursFragment())
                .addToBackStack(null)
                .commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("All tours");
    }

    private void showMyToursFragment() {
        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MyToursFragment(TOUR_AGENCY))
                .addToBackStack(null).commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("My tours");
    }

    private void showMapofArmenia() {
        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new MapFragment(locationPermissionsGrabted, PlaceInfoRepository.ZOOM_COUTRY, PlaceInfoRepository.getPlaceInfo(PlaceInfoRepository.ARMENIA)))
                .addToBackStack(null)
                .commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Map of Armenia");
    }


    private void showCurrentLocation() {
        getSupportFragmentManager().popBackStack();

        getLocationPermission();
        if (locationPermissionsGrabted) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new MapFragment(locationPermissionsGrabted))
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(this, " Cant show curent location permissions denied", Toast.LENGTH_SHORT).show();

        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Curent location");
        Toast.makeText(this, "TOURIST send item", Toast.LENGTH_SHORT).show();
    }


    private void showSignOutDialog() {
        String title = "";
        if (mAuth.getCurrentUser() != null) {
            title = mAuth.getCurrentUser().getEmail();
        }
        if (title.isEmpty()) {
            title = "Signing out";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(R.string.sign_out_dialog_message)
                .setIcon(R.drawable.ic_application)
                .setPositiveButton(R.string.action_sign_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog signOutDialog = builder.create();
        signOutDialog.show();
    }


    @SuppressLint("RestrictedApi")
    public void signOut() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();

            avatar_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_avatar));
            name_tv.setText(R.string.deafault_name);
            email_tv.setText(R.string.default_Email);
            TOUR_AGENCY = true;
            userUid = "";

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            invalidateOptionsMenu();
            showHomeFragment();
            repositoryForUserState.setIntoRep(UserState.NO_REGISTRATED);
        }
    }

        /*
         * ------------------------------MAP PERMISSIONS END GOOGLE MAP SERVICE CHECK----------------------------------------------------
         * */
        public boolean isServicesOK () {
            Log.d(TAG, "isServiceOk: checking google services version");

            int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

            if (available == ConnectionResult.SUCCESS) {
                // evriting ok can use map
                Log.d(TAG, "isServiceOk: Googgle play service working");
                return true;
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
                // ann eror that we can resolve it
                Log.d(TAG, "isServiceOk: ann eror but we can solve it");
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
                dialog.show();
            } else {
                Toast.makeText(this, "You cant use maps", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        private void getLocationPermission () {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionsGrabted = true;
                } else {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_COD);
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_COD);
            }
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            locationPermissionsGrabted = false;
            switch (requestCode) {
                case PERMISSION_REQUEST_COD: {
                    if (grantResults.length > 0) {
                        for (int i = 0; i < grantResults.length; i++) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                locationPermissionsGrabted = false;
                                return;
                            }
                        }
                        locationPermissionsGrabted = true;
                    }
                }
            }
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            Log.d("myMain", "onDestroy");
        }

        @Override
        protected void onPause () {
            super.onPause();
            Log.d("myMain", "onPause");
        }

        @Override
        protected void onResume () {
            super.onResume();
            getUserState();
            Log.d("myMain", "onResume");
        }


        private void getUserState () {
            final String key;
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                repositoryForUserState.setIntoRep(UserState.NO_REGISTRATED);
                Log.d("state", "inside getUserState method, not reg");

            } else {
                key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("Companies").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isCompany = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()
                        ) {
                            if (data.getKey().equals(key)) {
                                isCompany = true;
                                Log.d("state", "inside getUserState method, iscompany" + isCompany);
                                repositoryForUserState.setIntoRep(UserState.COMPANY);
                                break;
                            }
                        }
                        if (!isCompany) {
                            Log.d("state", "inside getUserState method, iscompany" + isCompany);
                            repositoryForUserState.setIntoRep(UserState.TOURIST);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

}


