package com.ArmGuide.tourapplication.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlaceKEY;
import com.ArmGuide.tourapplication.models.ServiceForNotification;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.Images.ImagesFragment;

import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfo;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment  {

    private Place place;
    private String placeKey;
    private UserStateViewModel userStateViewModel;

    BlankFragment(Place place, String placeKey) {
        this.place = place;
        this.placeKey = placeKey;
    }

    private TextView textViewViewMore, textViewDescription, textViewPlaceName, textViewViewTours;
    private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
    private CircleImageView circleImageView;
    private CheckBox checkBoxSubscribe;
    private Intent intentWeb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("MyLog", "BlankFragment - onCreateView" + placeKey);

        userStateViewModel = new UserStateViewModel();
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("MyLog", "BlankFragment - onViewCreated" + placeKey);

        Animation animationBackForward = AnimationUtils.loadAnimation(view.getContext(), R.anim.forward_back_anim);
        Animation animationPress = AnimationUtils.loadAnimation(view.getContext(), R.anim.press_anim);

        imageViewBack = view.findViewById(R.id.iv_LandscapeGoBack);
        imageViewForward = view.findViewById(R.id.iv_LandscapeGoForward);
        imageViewPressHand = view.findViewById(R.id.iv_pressIcon);
        circleImageView = view.findViewById(R.id.circleImageLand);
        imageViewMap = view.findViewById(R.id.ivMapLandScape);
        textViewViewMore = view.findViewById(R.id.tv_ViewMore);
        textViewPlaceName = view.findViewById(R.id.tv_LandscapeName);
        textViewDescription = view.findViewById(R.id.tv_LandscapeDescription);
        textViewViewTours = view.findViewById(R.id.tv_viewTours);
        checkBoxSubscribe = view.findViewById(R.id.checkBox_Subscribe);

        //<- and -> animation
        imageViewBack.startAnimation(animationBackForward);
        imageViewForward.startAnimation(animationBackForward);
        imageViewPressHand.startAnimation(animationPress);
        //
        List<String> keys = PlaceKEY.getInstance().getKeyList();
        if (placeKey.equals(keys.get(0)))
            imageViewBack.setVisibility(View.GONE);
        else if (placeKey.equals(keys.get(keys.size() - 1)))
            imageViewForward.setVisibility(View.GONE);

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
                ((FragmentActivity) view.getContext()).getSupportFragmentManager()
                        .beginTransaction().add(R.id.fragment_container, imagesFragment).addToBackStack(null).commit();
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
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new ToursByCategoryFragment(place.getName())).commit();
                }
                textViewViewTours.setTextSize(18);
            }
        });

        final PlaceInfo currentPlace = new PlaceInfo();
        com.google.android.gms.maps.model.LatLng currentLatLng = new LatLng(place.getCoord_X(),
                place.getCoord_Y());
        currentPlace.setName(place.getName());
        currentPlace.setId(place.getDescription());
        currentPlace.setLatLng(currentLatLng);

        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new MapFragment(true, PlaceInfoRepository.ZOOM_CITY,
                                            currentPlace))
                            .commit();
            }
        });


        userStateViewModel.getState().observe(BlankFragment.this, new Observer<UserState>() {
            @Override
            public void onChanged(UserState state) {

                Log.d("MyLog", "BlankFragment real state:" + state);
                if (state == UserState.COMPANY) {
                    checkBoxSubscribe.setVisibility(View.GONE);
                    checkBoxSubscribe.invalidate();
                } else {

                    if (state == UserState.TOURIST && getActivity() != null)
                        getActivity().startService(new Intent(getActivity(), ServiceForNotification.class));

                    checkBoxSubscribe.setVisibility(View.VISIBLE);
                    checkBoxSubscribe.invalidate();
                    final UserState userState = state;
                    checkBoxSubscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (userState == UserState.NO_REGISTRATED) {
                                checkBoxSubscribe.setChecked(false);
                                checkBoxSubscribe.invalidate();
                            } else if (userState == UserState.TOURIST) {
                                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tourists").
                                        child(userId).child("getSubscribedPlacesIds");

                                Log.d("reg", "onCheckedChangeListener = " + isChecked);
                                if (isChecked) {
                                    reference.child(placeKey).setValue(place.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("reg", "subscribed succeeded");
                                        }
                                    });
                                } else {
                                    reference.child(placeKey).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Log.d("reg", "subscribed removing succeeded");
                                        }
                                    });
                                }

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                                        ) {
                                            if (snapshot.getKey().equals(placeKey)) {
                                                checkBoxSubscribe.setChecked(true);
                                                checkBoxSubscribe.invalidate();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        textViewViewMore.setTextSize(15);
        textViewViewTours.setTextSize(15);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MyLog", "BlankFragment - onStart" + placeKey);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MyLog", "BlankFragment - onPause" + placeKey);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("MyLog", "BlankFragment - onStop" + placeKey);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyLog", "BlankFragment - onDestroy" + placeKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLog", "BlankFragment - onCreate" + placeKey);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("MyLog", "BlankFragment - onDestroyView" + placeKey);
    }



}
