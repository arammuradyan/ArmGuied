package com.ArmGuide.tourapplication.ui.Images;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapetZoomedImages extends RecyclerView.Adapter<AdapetZoomedImages.HolderZoomedImages> {


    private List<String> imagesUrls;

    public AdapetZoomedImages(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    class HolderZoomedImages extends RecyclerView.ViewHolder {
        ImageView imageViewZoomed;
        public HolderZoomedImages(@NonNull View itemView) {
            super(itemView);
            imageViewZoomed = itemView.findViewById(R.id.iv_ImageZoomed);
        }
    }

    @NonNull
    @Override
    public HolderZoomedImages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderZoomedImages(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_zoomedimage, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderZoomedImages holder, int position) {
        int positionInList = position % imagesUrls.size();
        Picasso.get().load(imagesUrls.get(positionInList)).placeholder(R.drawable.loading_placeholder).into(holder.imageViewZoomed);
    }


    @Override
    public int getItemCount() {
        return 1000;
    }

}
