package com.ArmGuide.tourapplication.ui.tours.by.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ToursRecyclerViewAdapter extends RecyclerView.Adapter<ToursRecyclerViewAdapter.ToursViewHolder> {
    protected List<String> names;

    public ToursRecyclerViewAdapter() {
        names = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tours_by_category_item,parent,false);
        return new ToursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToursViewHolder holder, int position) {
     String name=names.get(position);
     holder.bind(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    protected void setNames(List<String> namesList){
        names.addAll(namesList);
        notifyDataSetChanged();
    }

   protected static class ToursViewHolder extends RecyclerView.ViewHolder{
        private TextView agency_name_tv, tours_tv, price_tv, duration_tv;
        private ImageView  tour_category_img,agency_img;
        ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            viewInit(itemView);
        }

        private void bind(String name){
            agency_name_tv.setText(name);
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
