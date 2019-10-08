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
    private TextView textViewViewMore, textViewDescription, textViewPlaceName;
    private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
    private CircleImageView circleImageView;
    private Intent intentWeb;


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

    }


    @Override
    public void onResume() {
        super.onResume();
        textViewViewMore.setTextSize(13);
    }
}


