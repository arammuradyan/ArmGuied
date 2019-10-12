package com.ArmGuide.tourapplication.ui.tours.by.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ToursRecyclerViewAdapter extends RecyclerView.Adapter<ToursRecyclerViewAdapter.ToursViewHolder> {
    private List<Tour> tours;
    private OnToursViewHolderCLickListener onToursViewHolderCLickListener;

    public ToursRecyclerViewAdapter() {
        tours = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tours_by_category_item,parent,false);
        return new ToursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToursViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToursViewHolderCLickListener.onToursViewHolderClick(position);
            }
        });

        Tour tour=tours.get(position);
        holder.bind(tour);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public void setTours(List<Tour> toursList){
        tours.addAll(toursList);
        notifyDataSetChanged();
    }
    public void setOnToursViewHolderCLickListener(OnToursViewHolderCLickListener onToursViewHolderCLickListener){
        this.onToursViewHolderCLickListener=onToursViewHolderCLickListener;
    }

    public interface OnToursViewHolderCLickListener{
         void onToursViewHolderClick(int position);
    }

    public Tour getTour(int position){
        Tour tour=tours.get(position);
        return tour;
    }

   public static class ToursViewHolder extends RecyclerView.ViewHolder{
        private TextView agency_name_tv, tours_tv, price_tv, duration_tv;
        private ImageView  tour_category_img,agency_img;
        ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            viewInit(itemView);
        }

        private void bind(Tour tour){
            agency_name_tv.setText(tour.getTourCompany().getCompanyName());
            price_tv.setText(String.valueOf(tour.getPrice()));
            tours_tv.setText(tour.getPlaceName());
            duration_tv.setText(tour.getDate());

            if(tour.getImgUrl()!=null){
                if(!tour.getImgUrl().isEmpty())
                { Picasso.get().load(tour.getImgUrl())
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(tour_category_img);}
            }
            if(tour.getTourCompany().getAvatarUrl()!=null){
                if(!tour.getTourCompany().getAvatarUrl().isEmpty())
                { Picasso.get().load(tour.getTourCompany().getAvatarUrl())
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(agency_img);}
            }
        }

   private void viewInit(@NonNull View itemView){
       // text views
       agency_name_tv=itemView.findViewById(R.id.tour_agency_name_tv);
       tours_tv=itemView.findViewById(R.id.tours_tv);
       price_tv=itemView.findViewById(R.id.price_tv);
       duration_tv=itemView.findViewById(R.id.duration_tv);

       // imageview
       tour_category_img=itemView.findViewById(R.id.tour_category_img);
       agency_img=itemView.findViewById(R.id.tour_agency_img);

   }

    }
}
