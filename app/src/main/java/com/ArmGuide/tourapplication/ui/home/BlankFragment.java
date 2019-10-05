package com.ArmGuide.tourapplication.ui.home;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    public BlankFragment() {
    }

    private ConstraintLayout constraintLayout;
    private Animation animationBackForward, animationPress, animationPlaceIconAppear, animationFragmentClosing;
    private ObjectAnimator objectAnimatorScaleX, objectAnimatorScaleY, objectAnimatorPivotX, objectAnimatorPivotY;
    private AnimatorSet animatorSet;
    private TextView textViewLandScapeName, textViewDescription, textViewViewMore;
    private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand, imageViewPlace;
    private CircleImageView circleImageView;
    private CardView cardViewDescription;
    private Intent intentWeb;
    private AccelerateInterpolator accelerateInterpolator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //findViewById + anim
        {
            animationBackForward = AnimationUtils.loadAnimation(view.getContext(), R.anim.forward_back_anim);
            animationPress = AnimationUtils.loadAnimation(view.getContext(), R.anim.press_anim);
            animationPlaceIconAppear = AnimationUtils.loadAnimation(view.getContext(), R.anim.placeiconapear_anim);
            animationFragmentClosing = AnimationUtils.loadAnimation(view.getContext(), R.anim.handfragmentclosinganim);

            constraintLayout = view.findViewById(R.id.constraintLayoutChangeable);
            imageViewBack = view.findViewById(R.id.iv_LandscapeGoBack);
            imageViewForward = view.findViewById(R.id.iv_LandscapeGoForward);
            imageViewPressHand = view.findViewById(R.id.iv_pressIcon);
            imageViewPlace = view.findViewById(R.id.iv_placeIcon);
            circleImageView = view.findViewById(R.id.circleImageLand);
            imageViewMap = view.findViewById(R.id.ivMapLandScape);
            textViewLandScapeName = view.findViewById(R.id.tv_LandscapeName);
            textViewViewMore = view.findViewById(R.id.tv_ViewMore);
            textViewDescription = view.findViewById(R.id.tv_LandscapeDescription);
            cardViewDescription = view.findViewById(R.id.cardViewDescription);
        }


        //<- and -> animation
        imageViewBack.startAnimation(animationBackForward);
        imageViewForward.startAnimation(animationBackForward);
        imageViewPressHand.startAnimation(animationPress);
        //



        textViewViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentWeb = new Intent(view.getContext(), WebActivity.class);
                intentWeb.putExtra("uri", "https://en.wikipedia.org/wiki/Shikahogh_State_Reserve");
                startActivity(intentWeb);
            }
        });


    }
}


