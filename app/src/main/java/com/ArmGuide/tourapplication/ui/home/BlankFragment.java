package com.ArmGuide.tourapplication.ui.home;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Filter;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.PlaceKEY;
import com.ArmGuide.tourapplication.models.ServiceForFilteredNotifications;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.Images.ImagesFragment;

import com.ArmGuide.tourapplication.ui.map.MapFragment;
import com.ArmGuide.tourapplication.ui.map.PlaceInfo;
import com.ArmGuide.tourapplication.ui.map.PlaceInfoRepository;
import com.ArmGuide.tourapplication.ui.registr.LoginActivity;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private Place place;
    private boolean backPressed;
    private String placeKey;
    private UserStateViewModel userStateViewModel;
    private SubscribedPlacesViewModel subscribedPlacesViewModel;
    private FilterViewModel filterViewModel;
    private UserState userState;

    BlankFragment(Place place, String placeKey) {
        this.place = place;
        this.placeKey = placeKey;
    }


    private TextView textViewViewMore, textViewDescription, textViewPlaceName, textViewViewTours;
    private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
    private CircleImageView circleImageView;
    private CheckBox checkBoxSubscribe;
    private Intent intentWeb;
    private FilterBackPressedViewModel filterBackPressedViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterBackPressedViewModel = ViewModelProviders.of(requireActivity()).get(FilterBackPressedViewModel.class);
        Log.d("MyLog", "BlankFragment - onCreate" + placeKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("MyLog", "BlankFragment - onCreateView" + placeKey);

        userStateViewModel = new UserStateViewModel();
        subscribedPlacesViewModel = ViewModelProviders.of(BlankFragment.this).get(SubscribedPlacesViewModel.class);
        filterViewModel = ViewModelProviders.of(BlankFragment.this).get(FilterViewModel.class);
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
                Animatoo.animateSlideUp(view.getContext());

            }
        });
        textViewViewTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .add(R.id.fragment_container,
                                    new ToursByCategoryFragment(place)).commit();
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
                Log.d("mystt", "from Blankfr, in observer " + state);
                //checkBox GONE
                if (state == UserState.COMPANY && getActivity() != null) {
                    getActivity().stopService(new Intent(getActivity(), ServiceForFilteredNotifications.class));
                    checkBoxSubscribe.setVisibility(View.GONE);
                    checkBoxSubscribe.invalidate();
                }
                // DIALOG
                else if (state == UserState.NO_REGISTRATED && getActivity() != null) {
                    getActivity().stopService(new Intent(getActivity(), ServiceForFilteredNotifications.class));
                    checkBoxSubscribe.setVisibility(View.VISIBLE);
                    checkBoxSubscribe.setChecked(false);
                    checkBoxSubscribe.invalidate();
                    checkBoxSubscribe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkBoxSubscribe.setChecked(false);
                            AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                            ad.setTitle("Project requirements.")
                                    .setIcon(R.drawable.ic_info_black_24dp)
                                    .setMessage("You are going to subscribe on new tours, but haven't " +
                                            " logged in. Please enter your account to get notifications about new events!")
                                    .setCancelable(true)
                                    .setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            ad.show();
                        }
                    });
                }
                // Tourist
                else if (state == UserState.TOURIST && getActivity() != null) {
                    getActivity().startService(new Intent(getActivity(), ServiceForFilteredNotifications.class));

                    checkBoxSubscribe.setVisibility(View.VISIBLE);
                    checkBoxSubscribe.invalidate();


                    filterViewModel.getLiveData().observe(BlankFragment.this, new Observer<List<Filter>>() {
                        @Override
                        public void onChanged(List<Filter> filters) {
                            if (filters != null && filters.size() > 0) {
                                for (Filter f : filters
                                ) {
                                    if (f.getPlaceName().equals(place.getName())) {
                                        checkBoxSubscribe.setChecked(true);
                                        checkBoxSubscribe.invalidate();
                                        break;
                                    } else {
                                        checkBoxSubscribe.setChecked(false);
                                        checkBoxSubscribe.invalidate();
                                    }
                                }
                                Log.d("kkkl", "" + backPressed);
                                if (backPressed)
                                    checkBoxSubscribe.setChecked(false);
                            }
                        }
                    });

                    filterBackPressedViewModel.getLiveData().observe(requireActivity(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            checkBoxSubscribe.setChecked(false);
                        }
                    });


                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        final String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        checkBoxSubscribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkBoxSubscribe.isChecked()) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userKey", userKey);
                                    bundle.putString("placeName", place.getName());
                                    bundle.putString("placeKey", placeKey);
                                    FilterFragment filterFragment = new FilterFragment();
                                    filterFragment.setArguments(bundle);
                                    ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                            .add(R.id.fragment_container, filterFragment).addToBackStack(null).commit();
                                } else {
                                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tourists").
                                            child(userKey).child("SubscribedToursCriteria");
                                    reference.child(placeKey).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(view.getContext(), "You have successfully removed the subscription!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
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
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("MyLog", "BlankFragment - onDestroyView" + placeKey);
    }

    public void getBackPressedState(boolean bool) {
        backPressed = bool;
    }

}
