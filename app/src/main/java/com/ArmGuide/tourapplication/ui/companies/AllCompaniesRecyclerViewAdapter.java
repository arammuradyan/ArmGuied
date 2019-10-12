package com.ArmGuide.tourapplication.ui.companies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Company;
import com.ArmGuide.tourapplication.models.Tour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCompaniesRecyclerViewAdapter extends RecyclerView.Adapter<AllCompaniesRecyclerViewAdapter.ToursViewHolder> {
    private List<Company> companies;
    private OnToursViewHolderCLickListener onToursViewHolderCLickListener;

    public AllCompaniesRecyclerViewAdapter() {
        companies = new ArrayList<>();
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

        Company company=companies.get(position);
        holder.bind(company);
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void setCompanies(List<Company> companiesList){
        companies.addAll(companiesList);
        notifyDataSetChanged();
    }
    public void setOnToursViewHolderCLickListener(OnToursViewHolderCLickListener onToursViewHolderCLickListener){
        this.onToursViewHolderCLickListener=onToursViewHolderCLickListener;
    }

    public interface OnToursViewHolderCLickListener{
         void onToursViewHolderClick(int position);
    }

    public Company getCompany(int position){
        Company company=companies.get(position);
        return company;
    }

   public static class ToursViewHolder extends RecyclerView.ViewHolder{
        private TextView agency_name_tv, tours_tv, price_tv, duration_tv;
        private ImageView  tour_category_img,agency_img;

        ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            viewInit(itemView);
        }

        private void bind(Company company){
            agency_name_tv.setText(company.getCompanyName());
            price_tv.setText(String.valueOf(company.getPhoneNumber()));
            tours_tv.setText(company.getAddress());
            duration_tv.setText(company.getEmail());

            if(company.getAvatarUrl()!=null){
                if(!company.getAvatarUrl().isEmpty())
                { Picasso.get().load(company.getAvatarUrl())
                        .placeholder(R.drawable.ic_avatar)
                        .fit()
                        .centerCrop()
                        .into(tour_category_img);}
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
       agency_img.setVisibility(View.GONE);

   }

    }
}
