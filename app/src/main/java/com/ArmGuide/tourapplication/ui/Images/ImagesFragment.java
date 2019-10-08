package com.ArmGuide.tourapplication.ui.Images;


import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    Animation animationCloseImage, animationBackPrevious;
    RecyclerView recyclerViewImagesSmall, recyclerViewImageZoomed;
    CircleImageView circleImageLeft, circleImageRight, circleImageMiddle;
    ImageView imageViewIconClose, imageViewIconNext, imageViewIconPrevious;
    AdapetZoomedImages adapetZoomedImages;
    List<String> imageUrls;
    int position, positionLeft, positionRight;


    public ImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        imageUrls = getArguments().getStringArrayList("imageUrls");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleImageLeft = view.findViewById(R.id.circleImage_LeftImage);
        circleImageMiddle = view.findViewById(R.id.circleImage_MiddleImage);
        circleImageRight = view.findViewById(R.id.circleImage_RightImage);

        imageViewIconNext = view.findViewById(R.id.iv_iconNext);
        imageViewIconPrevious = view.findViewById(R.id.iv_iconPrevious);
        animationBackPrevious = AnimationUtils.loadAnimation(view.getContext(), R.anim.forward_back_anim);
        imageViewIconNext.startAnimation(animationBackPrevious);
        imageViewIconPrevious.startAnimation(animationBackPrevious);

        imageViewIconClose = view.findViewById(R.id.iv_iconTouch);
        animationCloseImage = AnimationUtils.loadAnimation(view.getContext(), R.anim.imageclose_anim);
        imageViewIconClose.startAnimation(animationCloseImage);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewImageZoomed = view.findViewById(R.id.recycler_view_ImageZoomed);
        recyclerViewImageZoomed.setHasFixedSize(true);
        recyclerViewImageZoomed.setLayoutManager(linearLayoutManager);
        adapetZoomedImages = new AdapetZoomedImages(imageUrls);
        recyclerViewImageZoomed.setAdapter(adapetZoomedImages);
        recyclerViewImageZoomed.getLayoutManager().scrollToPosition(500);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewImageZoomed);
        recyclerViewImageZoomed.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                int curPosition = holder.getAdapterPosition();

                position = curPosition % imageUrls.size();
                positionLeft = (curPosition - 1) % imageUrls.size();
                positionRight = (curPosition + 1) % imageUrls.size();

                Picasso.get().load(imageUrls.get(positionLeft)).into(circleImageLeft);
                Picasso.get().load(imageUrls.get(position)).into(circleImageMiddle);
                Picasso.get().load(imageUrls.get(positionRight)).into(circleImageRight);
            }
        });
        Picasso.get().load(imageUrls.get(imageUrls.size() - 1)).into(circleImageLeft);
        Picasso.get().load(imageUrls.get(0)).into(circleImageMiddle);
        Picasso.get().load(imageUrls.get(1)).into(circleImageRight);

        recyclerViewImageZoomed.setOnTouchListener(new View.OnTouchListener() {
            float startPosition_Y;
            float endPosition_Y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPosition_Y = event.getY();
                    case MotionEvent.ACTION_UP:
                        endPosition_Y = event.getY();
                        break;
                    default:
                        break;
                }
                Log.d("MyLog", "start - " + startPosition_Y + " end " + endPosition_Y + "dif" + (endPosition_Y - startPosition_Y));
                if (endPosition_Y - startPosition_Y > 250f && startPosition_Y > 500f)
                    // Toast.makeText(view.getContext(), "ok", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                return false;
            }
        });
    }

}
