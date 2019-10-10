package com.ArmGuide.tourapplication.ui.home;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.UserState;
import com.ArmGuide.tourapplication.ui.Images.ImagesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerBlank extends RecyclerView.Adapter<AdapterRecyclerBlank.BlankViewHolder> {

    private List<Place> places, placeList;
    private UserState state, userState;
    private String url_Wiki;
    private List<String> url_Images;
    private Animation animationBackForward, animationPress;

    public AdapterRecyclerBlank(List<Place> places, UserState state) {
        this.places = places;
        this.state = state;
        placeList = new ArrayList<>();
        init();
        Log.d("rec","constructor() / placeList - "+placeList.size()+" / userState - "+userState);
    }

    public void setPlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        init();
        Log.d("rec","setPlaces() / placeList - "+placeList.size()+" / userState - "+userState);
    }

    public void init() {
        if (places != null && state != null) {
            placeList.clear();
            placeList.addAll(places);
            userState = state;
            notifyDataSetChanged();
        }
    }

    public void setUserState(UserState state) {
        this.state = state;
        init();
        Log.d("rec","setUserState() / placeList - "+placeList.size()+" / userState - "+userState);

    }

    class BlankViewHolder extends RecyclerView.ViewHolder {


        private TextView textViewViewMore, textViewDescription, textViewPlaceName,textViewViewTours;
        private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
        private CircleImageView circleImageView;
        private CheckBox checkBoxSubscribe;

        public BlankViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewViewMore = itemView.findViewById(R.id.tv_ViewMore);
            textViewDescription = itemView.findViewById(R.id.tv_LandscapeDescription);
            textViewPlaceName = itemView.findViewById(R.id.tv_LandscapeName);
            imageViewBack = itemView.findViewById(R.id.iv_LandscapeGoBack);
            imageViewForward = itemView.findViewById(R.id.iv_LandscapeGoForward);
            imageViewMap = itemView.findViewById(R.id.ivMapLandScape);
            imageViewPressHand = itemView.findViewById(R.id.iv_pressIcon);
            circleImageView = itemView.findViewById(R.id.circleImageLand);
            textViewViewTours = itemView.findViewById(R.id.tv_viewTours);
            checkBoxSubscribe = itemView.findViewById(R.id.checkBox_Subscribe);



            textViewViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViewViewMore.setTextSize(18);
                    Intent intentWeb = new Intent(view.getContext(), WebActivity.class);
                    intentWeb.putExtra("uri", url_Wiki);
                    itemView.getContext().startActivity(intentWeb);
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("imageUrls", (ArrayList<String>) url_Images);
                    ImagesFragment imagesFragment = new ImagesFragment();
                    imagesFragment.setArguments(bundle);
                    ((FragmentActivity) itemView.getContext()).getSupportFragmentManager()
                            .beginTransaction().add(R.id.fragment_container, imagesFragment).addToBackStack(null).commit();
                }
            });

            //<- and -> animation
            animationBackForward = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.forward_back_anim);
            animationPress = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.press_anim);

            if(userState==UserState.COMPANY)
                checkBoxSubscribe.setVisibility(View.GONE);

        }
    }

    @NonNull
    @Override
    public BlankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlankViewHolder holder, int position) {
        Place currentPlace = placeList.get(position);

        holder.textViewPlaceName.setText(currentPlace.getName());
        holder.textViewDescription.setText(currentPlace.getDescription());
        Picasso.get().load(currentPlace.getImageUrls().get(0)).placeholder(R.drawable.loading_placeholder).into(holder.circleImageView);

        url_Wiki = currentPlace.getUrl_Wiki();
        Log.d("wiki",""+url_Wiki  + currentPlace.getName() + position);
        url_Images = currentPlace.getImageUrls();
        holder.imageViewBack.startAnimation(animationBackForward);
        holder.imageViewForward.startAnimation(animationBackForward);
        holder.imageViewPressHand.startAnimation(animationPress);

    }

    @Override
    public int getItemCount() {
        return places != null ? places.size() : 0;
    }
}
