package com.ArmGuide.tourapplication.ui.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ViewModels.ViewModelBlankFragment;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlacesNames;
import com.ArmGuide.tourapplication.ui.Images.ImagesFragment;

import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfo;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private Place place;

    public BlankFragment(Place place) {
        this.place = place;
    }

    private Animation animationBackForward, animationPress;
    private TextView textViewViewMore, textViewDescription, textViewPlaceName, textViewViewTours;
    private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
    private CircleImageView circleImageView;
    private Intent intentWeb;


    //TODO Constructorov tal tvyal Place - i id in vorov
    // kgtni placin hamapatasxan turer@ u Place - i informacian Mapi vra cuyc talu hamar

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animationBackForward = AnimationUtils.loadAnimation(view.getContext(), R.anim.forward_back_anim);
        animationPress = AnimationUtils.loadAnimation(view.getContext(), R.anim.press_anim);

        imageViewBack = view.findViewById(R.id.iv_LandscapeGoBack);
        imageViewForward = view.findViewById(R.id.iv_LandscapeGoForward);
        imageViewPressHand = view.findViewById(R.id.iv_pressIcon);
        circleImageView = view.findViewById(R.id.circleImageLand);
        imageViewMap = view.findViewById(R.id.ivMapLandScape);
        textViewViewMore = view.findViewById(R.id.tv_ViewMore);
        textViewPlaceName = view.findViewById(R.id.tv_LandscapeName);
        textViewDescription = view.findViewById(R.id.tv_LandscapeDescription);
        textViewViewTours = view.findViewById(R.id.tv_viewTours);


        //<- and -> animation
        imageViewBack.startAnimation(animationBackForward);
        imageViewForward.startAnimation(animationBackForward);
        imageViewPressHand.startAnimation(animationPress);
        //

        textViewPlaceName.setText(place.getName());
        textViewDescription.setText(place.getDescription());
        Picasso.get().load(place.getImageUrls().get(0)).placeholder(R.drawable.loading_placeholder).into(circleImageView);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imageUrls", (ArrayList<String>) place.getImageUrls());
                ImagesFragment imagesFragment = new ImagesFragment();
                imagesFragment.setArguments(bundle);
                ((FragmentActivity)view.getContext()).getSupportFragmentManager()
                        .beginTransaction().add(R.id.fragment_container,imagesFragment).addToBackStack(null).commit();
            }
        });

        textViewViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewViewMore.setTextSize(18);
                intentWeb = new Intent(view.getContext(), WebActivity.class);
                intentWeb.putExtra("uri", place.getUrl_Wiki());
                startActivity(intentWeb);
            }
        });

        textViewViewTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new ToursByCategoryFragment(place.getName())).commit();
                }
            }
        });

        final PlaceInfo currentPlace=new PlaceInfo();
        com.google.android.gms.maps.model.LatLng currentLatLng= new LatLng(place.getCoord_X(),
                place.getCoord_Y());
        currentPlace.setName(place.getName());
        currentPlace.setId(place.getDescription());
        currentPlace.setLatLng(currentLatLng);

        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new MapFragment(true,PlaceInfoRepository.ZOOM_CITY,
                                    currentPlace))
                            .commit();

            }
        });

      //  final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tourists").
//                child(userId).child("getSubscribedPlacesIds");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()
//                ) {
//                    if (snapshot.getKey().equals(placeKey)) {
//                        checkBoxSubscribe.setChecked(true);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        if (state == UserState.COMPANY)
//            checkBoxSubscribe.setVisibility(View.GONE);
//        else {
//            checkBoxSubscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (state == UserState.NO_REGISTRATED) {
//                        checkBoxSubscribe.setChecked(false);
//                    } else {
//                        Log.d("reg", "onCheckedChangeListener = " + isChecked);
//                        if (isChecked) {
//                            reference.child(placeKey).setValue(placeKey).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Log.d("reg", "subscribed succeeded");
//                                }
//                            });
//                        } else {
//                            reference.child(placeKey).removeValue(new DatabaseReference.CompletionListener() {
//                                @Override
//                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                                    Log.d("reg", "subscribed removing succeeded");
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
        textViewViewMore.setTextSize(13);
    }
}


