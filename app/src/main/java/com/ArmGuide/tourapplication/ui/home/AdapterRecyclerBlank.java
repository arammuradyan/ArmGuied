package com.ArmGuide.tourapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.WebActivity;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.ui.Images.ImagesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerBlank extends RecyclerView.Adapter<AdapterRecyclerBlank.BlankViewHolder> {

    private List<Place> places;

    private String url_Wiki;
    private List<String> url_Images;
    private Animation animationBackForward, animationPress;

    public AdapterRecyclerBlank(List<Place> places) {
        this.places = places;
    }

    class BlankViewHolder extends RecyclerView.ViewHolder {


        private TextView textViewViewMore, textViewDescription, textViewPlaceName;
        private ImageView imageViewBack, imageViewForward, imageViewMap, imageViewPressHand;
        private CircleImageView circleImageView;

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

            imageViewBack.startAnimation(animationBackForward);
            imageViewForward.startAnimation(animationBackForward);
            imageViewPressHand.startAnimation(animationPress);
            //
        }
    }

    @NonNull
    @Override
    public BlankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlankViewHolder holder, int position) {
        Place currentPlace = places.get(position);

        holder.textViewPlaceName.setText(currentPlace.getName());
        holder.textViewDescription.setText(currentPlace.getDescription());
        Picasso.get().load(currentPlace.getImageUrls().get(0)).placeholder(R.drawable.loading_placeholder).into(holder.circleImageView);
        url_Wiki = currentPlace.getUrl_Wiki();
        url_Images = currentPlace.getImageUrls();

    }

    @Override
    public int getItemCount() {
        return places != null ? places.size() : 0;
    }
}
