package com.ArmGuide.tourapplication.ui.myTours;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyToursRecyclerViewAdapter extends RecyclerView.Adapter<MyToursRecyclerViewAdapter.ToursViewHolder> {
    private List<Tour> tours;
    private OnToursViewHolderCLickListener onToursViewHolderCLickListener;
    private boolean TOUR_AGENCY;

    public MyToursRecyclerViewAdapter() {
        tours = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_tours_item,parent,false);
        return new ToursViewHolder(view,TOUR_AGENCY,onToursViewHolderCLickListener);
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
        tours.clear();
        tours.addAll(toursList);
        notifyDataSetChanged();
    }

    public void setTOUR_AGENCY(boolean TOUR_AGENCY){
        this.TOUR_AGENCY=TOUR_AGENCY;
    }

    public void setOnToursViewHolderCLickListener(OnToursViewHolderCLickListener onToursViewHolderCLickListener){
        this.onToursViewHolderCLickListener=onToursViewHolderCLickListener;
    }

    public interface OnToursViewHolderCLickListener{
         void onToursViewHolderClick(int position);
         void onEditButtonClick(int position);
    }

    public Tour getTour(int position){
        Tour tour=tours.get(position);
        return tour;
    }

   public static class ToursViewHolder extends RecyclerView.ViewHolder{
        private TextView agency_name_tv, tours_tv, price_tv, duration_tv;
        private ImageView  tour_category_img,agency_img;
        private ImageButton edit_btn;

        private boolean TOUR_AGENCY;
        private OnToursViewHolderCLickListener onToursViewHolderCLickListener;


       ToursViewHolder(@NonNull View itemView,boolean TOUR_AGENCY,OnToursViewHolderCLickListener onToursViewHolderCLickListener) {
            super(itemView);
            this.TOUR_AGENCY=TOUR_AGENCY;
            this.onToursViewHolderCLickListener=onToursViewHolderCLickListener;

            viewInit(itemView);
       }

        private void bind(Tour tour){
            agency_name_tv.setText(tour.getTourCompany().getCompanyName());
            price_tv.setText(String.valueOf(tour.getPrice()));
            tours_tv.setText(tour.getPlaceName());

            duration_tv.setTextColor(duration_tv.getContext().getResources().getColor(R.color.colorBlack));


            if(tour.getMoreInfo().equals("COMPANY HAS DELETED THIS TOUR")){
                duration_tv.setTextColor(duration_tv.getContext().getResources().getColor(R.color.colorRed));
                duration_tv.setText(tour.getMoreInfo());
            }else{
            duration_tv.setText(tour.getDate());
            }


            if(tour.getImgUrl()!=null){
                if(!tour.getImgUrl().isEmpty())
                { Picasso.get().load(tour.getImgUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(tour_category_img);}
            }
           if(tour.getTourCompany().getAvatarUrl()!=null){
                if(!tour.getTourCompany().getAvatarUrl().isEmpty())
                { Picasso.get().load(tour.getTourCompany().getAvatarUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(agency_img);}
            }

           edit_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onToursViewHolderCLickListener.onEditButtonClick(getAdapterPosition());
               }
           });
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
       //imagebutton
       edit_btn=itemView.findViewById(R.id.edit_btn);
      /* if(TOUR_AGENCY){
           edit_btn.setVisibility(View.VISIBLE);
       }*/

   }

    }
}
