package com.ArmGuide.tourapplication.ui.map;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapsInitializer;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//import com.google.android.libraries.places.api.internal.impl.net.pablo.PlaceResult;


import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionsGranted=false;
    private static final int PLACEPICKER_REQUEST_CODE=789;



    private PlaceInfo placeInfo;
    private Marker placeMarker;
    private MarkerOptions optionsMylocation;

    // views
    private ImageView gps_img;
    private ImageView info_img;
    private ImageView place_picker_img;

    // kanstruktrum nshvac parametrerov cameraMove anelu hamar
    private float zoom;
    private PlaceInfo placeInfoFromActivity=null;


    public MapFragment(boolean locationPermissionsGranted ) {
    this.locationPermissionsGranted=locationPermissionsGranted;
    }

    public MapFragment(boolean locationPermissionsGranted ,/*LatLng latlng,*/float zoom,PlaceInfo placeInfoFromActivity) {
    this.locationPermissionsGranted=locationPermissionsGranted;
    this.zoom=zoom;
    this.placeInfoFromActivity=placeInfoFromActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_map, container, false);

        initMap();
        initPlacies();
        initViews(view);
        setButtonsClickListeners();

        return  view;
    }

    private void initViews(View view){
        gps_img=view.findViewById(R.id.ic_gps);
        info_img=view.findViewById(R.id.ic_place_info);
        place_picker_img=view.findViewById(R.id.ic_place_picker);
    }
    private void setButtonsClickListeners(){
        gps_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
                placeInfoFromActivity=null;
            }
        });
        info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(placeMarker.isInfoWindowShown()){
                        placeMarker.hideInfoWindow();
                    }else{
                        placeMarker.showInfoWindow();
                        Toast.makeText(getActivity(),"PLACIES show info show info",Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getActivity(),"PLACIES show info "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        place_picker_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(placeInfoFromActivity!=null){
                    startNearByPlacesForChoosenPlace();
               }
                else{
                   Intent intent=new Intent(Intent.ACTION_VIEW);
                   Uri gmmIntentUri = Uri.parse("geo:0,0");
                   intent.setData(gmmIntentUri);
                   startActivity(intent);

                   startNearByPlacesForLocation();
                }*/
                placePicker();
            }
        });

    }
    private void startNearByPlacesForChoosenPlace(){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        double latitude= placeInfoFromActivity.getLatLng().latitude;
        double longitude= placeInfoFromActivity.getLatLng().longitude;
        String uri="geo:"+latitude+","+longitude;


        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void startNearByPlacesForLocation(){
        if(locationPermissionsGranted){
            Task location=fusedLocationProviderClient.getLastLocation();

            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location currentLocation=(Location)task.getResult();
                        if(currentLocation!=null){
                            String curentLocationForPlacePicker="";

                            double lat=currentLocation.getLatitude();
                            double lng=currentLocation.getLongitude();
                            curentLocationForPlacePicker+="geo:"+lat+","+lng;
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(curentLocationForPlacePicker));
                            startActivity(intent);

                            Toast.makeText(getActivity(),curentLocationForPlacePicker,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),"OnCoplite bot working",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void initMap() {
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if(mapFragment!=null) {
            mapFragment.getMapAsync(MapFragment.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(getActivity()!=null)
            MapsInitializer.initialize(getActivity());
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Toast.makeText(getActivity(),"ON MAP READY",Toast.LENGTH_SHORT).show();

        if(locationPermissionsGranted && placeInfoFromActivity==null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Toast.makeText(getActivity(),"ON MAP READY"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    getDeviceLocation();
                }
            }).start();

            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
            mGoogleMap.clear();
            // showing cuurent location by blue dot and adds back to location button
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }else{

            final com.google.android.libraries.maps.model.LatLng
                    placeLatlng=new
                    com.google.android.libraries.maps.model.LatLng( placeInfoFromActivity.getLatLng().latitude,
                    placeInfoFromActivity.getLatLng().longitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Toast.makeText(getActivity(),"ON MAP READY"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    if(getActivity()!=null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                moveCamera(placeLatlng,zoom,placeInfoFromActivity);

                            }
                        });
                }
            }).start();
        }

    }

    private void getDeviceLocation(){
//        Toast.makeText(getActivity(),"getDeviceLocation :Metode",Toast.LENGTH_SHORT).show();
     fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());

     try{
          if(locationPermissionsGranted){
              Task location=fusedLocationProviderClient.getLastLocation();
              location.addOnCompleteListener(new OnCompleteListener() {
                  @Override
                  public void onComplete(@NonNull Task task) {
                      if(task.isSuccessful()){
                         // Toast.makeText(getActivity(),"onComplite: location found",Toast.LENGTH_SHORT).show();
                          Location currentLocation=(Location)task.getResult();
                           if(currentLocation!=null){

                               moveCamera(new LatLng(currentLocation.getLatitude(),
                                  currentLocation.getLongitude()),
                                  PlaceInfoRepository.ZOOM_STREETS,
                                  "My location");
                               if(optionsMylocation==null){
                               optionsMylocation=new MarkerOptions()
                                       .position(new LatLng(currentLocation.getLatitude(),
                                               currentLocation.getLongitude()))
                                       .title("My location");
                               mGoogleMap.addMarker(optionsMylocation);}


                           }else{
                              // Toast.makeText(getActivity(),"location = null",Toast.LENGTH_SHORT).show();

                           }
                      }else{
                          //Toast.makeText(getActivity(),"unable get curent location",Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
     }catch (SecurityException e){
        // Toast.makeText(getActivity(),"SecurityException"+e.getMessage(),Toast.LENGTH_SHORT).show();

     }
    }

  // MOVE TO CURRENT LOCATION
    private void moveCamera(LatLng latlng, float zoom, String title){
        // moving camera to latlng
        Toast.makeText(getActivity(),"moveCamera"+latlng.toString(),Toast.LENGTH_SHORT).show();

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom),3000,null);

        /*MarkerOptions options=new MarkerOptions()
                .position(latlng)
                .title(title);
        mGoogleMap.addMarker(options);*/
    }



/*

--------------------------------------------------- PLACIES ---------------------------------------------------------------------------



 PLACE SEARCH AUTOCOMPLITE
*/
private void initPlacies(){

    if (!com.google.android.libraries.places.api.Places.isInitialized()) {
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyBQowSumhHgacPo_LedaEI9OyO1wcPRoDU");
    }

//     placeAutocomplite;
    AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
            getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

    autocompleteFragment.setPlaceFields(Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.USER_RATINGS_TOTAL));

    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {
            try{
                placeInfo=new PlaceInfo();
            placeInfo.setId(place.getId());
            placeInfo.setName(place.getName());
            placeInfo.setAddress(place.getAddress());
            placeInfo.setReiting(place.getRating());
            placeInfo.setUserReitingsTotal(place.getUserRatingsTotal());
            placeInfo.setPhoneNumber(place.getPhoneNumber());
            placeInfo.setWebsiteUri(place.getWebsiteUri());
            placeInfo.setLatLng(place.getLatLng());

            Toast.makeText(getActivity(),"onPlaceSelected"+placeInfo.toString(),Toast.LENGTH_SHORT).show();
            }
            catch (NullPointerException e){
                Toast.makeText(getActivity(),"onPlaceSelected"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            // MOVING CAMERA TO COOSEN PLACE
            moveCamera(new LatLng(placeInfo.getLatLng().latitude,
                    placeInfo.getLatLng().longitude),
                    PlaceInfoRepository.ZOOM_CITY,
                    placeInfo);
         }
         @Override
        public void onError(@NonNull Status status) {
            Toast.makeText(getActivity(),"onEror"+status.getStatusMessage(),Toast.LENGTH_LONG).show();
            Log.d("TAG","onEror : "+status.getStatusMessage());
        }
    });
}


   // MOVES CAMER TO CHOOSEN PLACE

    private void moveCamera(LatLng latlng, float zoom, PlaceInfo placeInfo){
        // moving camera to latlng prom PLACE AUTOCOMPLITE
        //Toast.makeText(getActivity(),"moveCamera"+latlng.toString(),Toast.LENGTH_SHORT).show();
       // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));


        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latlng,zoom),
                3000,
                null);
        mGoogleMap.setInfoWindowAdapter(new PlaceInfoWindowAdapter(getActivity()));

        if(placeInfo!=null){
            try{
                String description=
                        "Address: "+placeInfo.getAddress()+"\n"+
                        "Phonenumber: "+placeInfo.getPhoneNumber()+"\n"+
                        "Rating: "+placeInfo.getReiting()+"\n"+
                        "Total Users Rating: "+placeInfo.getUserReitingsTotal()+"\n"+
                        "Website: "+placeInfo.getWebsiteUri()+"\n" +
                        "Latlng: "+placeInfo.getLatLng()+"\n" ;
                MarkerOptions markerOptions= new MarkerOptions()
                        .position(latlng)
                        .title(placeInfo.getName())
                        .snippet(description);

             placeMarker=mGoogleMap.addMarker(markerOptions);
            }catch (NullPointerException e){
               // Toast.makeText(getActivity(),"PLACIES moveCamera "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else{
            mGoogleMap.addMarker(new MarkerOptions().position(latlng));
        }
        MarkerOptions options=new MarkerOptions()
                .position(latlng)
                .title(placeInfo.getName()+"/n"+placeInfo.getAddress());
        mGoogleMap.addMarker(options);
    }


   /*
   * ------------------------------------------ PLACE PICKER----------------------------------------------------------------------------
   * */

    private void placePicker(){
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

    if(getActivity()!=null){
   // Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
     //       .build(getActivity());

        Intent intent= null;
        try {
            intent = new PlacePicker.IntentBuilder()
                    .build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        // Intent intent=new Intent("com.google.android.gms.location.places.ui.PICK_PLACE");

    startActivityForResult(intent, PLACEPICKER_REQUEST_CODE);}
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACEPICKER_REQUEST_CODE && data !=null) {
            if (resultCode == RESULT_OK) {

               //Place place = Autocomplete.getPlaceFromIntent(data);

                com.google.android.gms.location.places.Place place=PlacePicker.getPlace(getActivity(),data);

                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
               // tvPlaceDetails.setText(stBuilder.toString());
                Toast.makeText(getActivity(),"PLAC Picker "+place.getName() + ", " + place.getId(),Toast.LENGTH_SHORT).show();
            } else if (resultCode ==PlacePicker.RESULT_ERROR) {
                // TODO: Handle the error.
               // Status status = Autocomplete.getStatusFromIntent(data);
               String string=PlacePicker.getStatus(getActivity(),data).toString();

                Toast.makeText(getActivity(),"PLAC Picker RESULT_ERROR "+string /*status.getStatusMessage()*/,Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }





    /*
     * -------------------------------------------------------------------- FRAGMENT LIFCYCLE METHODS---------------------------------------------------------------------------
     * */

    @Override
    public void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        // mGoogleMap.clear();
    }
    @Override
    public void onResume() {
        super.onResume();
        // initMap();
        // initPlacies();
    }


}








































//TODO: init()-i takic cut arac
// com.google.android.libraries.places.compat.Places.initialize(getApplicationContext(), apiKey);

// Create a new Places client instance
// PlacesClient placesClient = Places.createClient(this);


    /*   googleApiClient= new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(),this)
                .build();
        placeAutocompleteAdapter= new PlaceAutocompleteAdapter(getActivity(),googleApiClient,LAT_LNG_BOUNDS,null);
        search_et.setAdapter(placeAutocompleteAdapter);*/


       /* search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId== EditorInfo.IME_ACTION_DONE
                || event.getAction()==KeyEvent.ACTION_DOWN || event.getAction()==KeyEvent.KEYCODE_ENTER){
                   // searching enyting inputed in edit text
                    geoLocate();
                }
                return false;
            }
        });*/



       // onCreateView-i mejic



        /*mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(com.google.android.gms.location.places.Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
mGoogleApiClient.connect();*/

       /* serch_img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               geoLocate();
           }
       });*/



       // PLACE SEARCH- i hamar hnacac metod

        /*private void geoLocate(){
        String searchInput= search_et.getText().toString();
        Geocoder geocoder= new Geocoder(getActivity());
        List<Address> list =new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchInput,1);
        }catch(IOException e){
            Toast.makeText(getActivity(),"geoLocate"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        if(list.size()>0){
            Address address= list.get(0);
            Toast.makeText(getActivity(),address.toString()+"Adrees searched",Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFOULT_ZOOM,address.getAddressLine(0));

        }
    }*/


        //  private PlacesClient placesClient;
    /*private static final com.google.android.gms.maps.model.LatLngBounds LAT_LNG_BOUNDS=
            new LatLngBounds(new com.google.android.gms.maps.model.LatLng(40,168)
            ,new com.google.android.gms.maps.model.LatLng(71,136));*/
// poxel myusin ete chashxati
  /*  private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;*/
//GoogleApiClient mGoogleApiClient;




