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
import com.ArmGuide.tourapplication.models.Tourist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyToursTouristsListRecyclerViewAdapter extends RecyclerView.Adapter<MyToursTouristsListRecyclerViewAdapter.ToursViewHolder> {
    private List<Tourist> tourists;
    private OnImageButtonsCLickListener onImageButtonsCLickListener;

    public MyToursTouristsListRecyclerViewAdapter() {
        tourists = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tourists_list_rv_item,parent,false);
        return new ToursViewHolder(view,onImageButtonsCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ToursViewHolder holder, final int position) {



        Tourist tourist=tourists.get(position);
        holder.bind(tourist);
    }

    @Override
    public int getItemCount() {
        return tourists.size();
    }

    public void setTourists(List<Tourist> touristsList){
        tourists.clear();
        tourists.addAll(touristsList);
        notifyDataSetChanged();
    }
    public Tourist getTourist(int index){
        return tourists.get(index);
    }
    public void setOnImageButtonsCLickListener(OnImageButtonsCLickListener onImageButtonsCLickListener){
        this.onImageButtonsCLickListener = onImageButtonsCLickListener;
    }

    public interface OnImageButtonsCLickListener {
         void onImageButtonsClick(int position,int id);
    }

    public static class ToursViewHolder extends RecyclerView.ViewHolder{
        private TextView tourist_name_tv, phone_tv, email_tv;
        private ImageView  tourist_avatar_img;
        private ImageButton call_btn, email_btn;

        private OnImageButtonsCLickListener onImageButtonsCLickListener;

        ToursViewHolder(@NonNull View itemView,OnImageButtonsCLickListener onImageButtonsCLickListener) {
            super(itemView);
            this.onImageButtonsCLickListener=onImageButtonsCLickListener;

            viewInit(itemView);
        }

       private void viewInit(@NonNull View itemView){
           // text views
           tourist_name_tv=itemView.findViewById(R.id.rv_name_tv);
           phone_tv=itemView.findViewById(R.id.rv_number_tv);
           email_tv=itemView.findViewById(R.id.rv_adress_tv);

           // imageview
           tourist_avatar_img=itemView.findViewById(R.id.rv_img);

           //Image buttons
           call_btn=itemView.findViewById(R.id.call_btn);
           email_btn=itemView.findViewById(R.id.send_email_btn);

           call_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onImageButtonsCLickListener.onImageButtonsClick(getAdapterPosition(),v.getId());
               }
           });
           email_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onImageButtonsCLickListener.onImageButtonsClick(getAdapterPosition(),v.getId());

               }
           });

       }
        private void bind(Tourist tourist){
            if(tourist.getAvatarUrl()!=null){
                if(!tourist.getAvatarUrl().isEmpty())
                { Picasso.get().load(tourist.getAvatarUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(tourist_avatar_img);}
            }
            tourist_name_tv.setText(tourist.getFullName().trim());
            phone_tv.setText(tourist.getPhoneNumber().trim());
            email_tv.setText(tourist.getEmail().trim());
        }



    }
}
