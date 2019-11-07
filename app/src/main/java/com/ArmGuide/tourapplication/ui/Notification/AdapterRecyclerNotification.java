package com.ArmGuide.tourapplication.ui.Notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;

public class AdapterRecyclerNotification extends RecyclerView.Adapter<AdapterRecyclerNotification.MyHolder> implements RVHAdapter {

    private List<Tour> tours;
    private Fragment fragment;
    private String userKey;


    public AdapterRecyclerNotification(Fragment fragment, String userKey) {
        this.fragment = fragment;
        homeViewModel = new HomeViewModel();
        tours = new ArrayList<>();
        this.userKey = userKey;
    }

    private HomeViewModel homeViewModel;

    class MyHolder extends RecyclerView.ViewHolder {

        TextView textViewPlaceName, textViewAgencyName, textViewPrice, textViewDate,
                textViewTransport, textViewFood, textViewGuide, textViewWine, textViewWifi;
        TextView buttonAddMyTours;
        ImageView imageViewNot;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlaceName = itemView.findViewById(R.id.tv_placeNameNot);
            textViewAgencyName = itemView.findViewById(R.id.tv_tourCompanyNameNot);
            textViewPrice = itemView.findViewById(R.id.tv_PriceNot);
            textViewDate = itemView.findViewById(R.id.tv_dateNot);
            textViewTransport = itemView.findViewById(R.id.tv_transportNot);
            textViewFood = itemView.findViewById(R.id.tv_foodNot);
            textViewGuide = itemView.findViewById(R.id.tv_guideNot);
            textViewWifi = itemView.findViewById(R.id.tv_wifiNot);
            textViewWine = itemView.findViewById(R.id.tv_wineNot);
            buttonAddMyTours = itemView.findViewById(R.id.btn_AddToMyTours);
            imageViewNot = itemView.findViewById(R.id.imageViewNot);


        }
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclernotification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final Tour curTour = tours.get(position);

        String agencyText = "Tour agency :   " + curTour.getTourCompany().getCompanyName();
        String priceText = "Price :   " + curTour.getPrice() + " AMD";
        String dateText = "Date: from " + curTour.getDate() + " to " + curTour.getEndDate();
        holder.textViewPlaceName.setText(curTour.getPlaceName());
        holder.textViewAgencyName.setText(agencyText);
        holder.textViewPrice.setText(priceText);
        holder.textViewDate.setText(dateText);
        if (!curTour.isTransport())
            holder.textViewTransport.setVisibility(View.GONE);
        if (!curTour.isFood())
            holder.textViewFood.setVisibility(View.GONE);
        if (!curTour.isThreeLangGuide())
            holder.textViewGuide.setVisibility(View.GONE);
        if (!curTour.isWifi())
            holder.textViewWifi.setVisibility(View.GONE);
        if (!curTour.isVineDegustation())
            holder.textViewWine.setVisibility(View.GONE);

        homeViewModel.getLiveData().observe(fragment, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                String imageUrl = "";
                for (Place p : places
                ) {
                    if (p.getName().equals(curTour.getPlaceName())) {
                        imageUrl = p.getImageUrls().get(2);
                        break;
                    }
                }
                Picasso.get().load(imageUrl).fit().centerCrop().placeholder(R.drawable.loading_placeholder).into(holder.imageViewNot);
            }
        });


        holder.buttonAddMyTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(fragment.getContext(), "Exaca", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                        .child("tours").addValueEventListener(new ValueEventListener() {
                    List<Tour> toursFormFB = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()
                        ) {
                            if(data!=null) {
                                Tour t = data.getValue(Tour.class);
                                toursFormFB.add(t);
                                if (t.getId().equals(tours.get(position).getId())) {
                                    exists = true;
                                    if (fragment.getContext() != null)
                                        Toast.makeText(fragment.getContext(), "The Tour already exists in 'My Tours'.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        if (!exists) {
                            toursFormFB.add(tours.get(position));
                            FirebaseDatabase.getInstance().getReference().child("Tourists")
                                    .child(userKey).child("tours").setValue(toursFormFB)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (fragment.getContext() != null)
                                                Toast.makeText(fragment.getContext(), "The Tour has successfully been added into 'My Tours'.",
                                                        Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        toursFormFB.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }


    public void setTours(List<Tour> tours) {
        if (tours.size() > 0) {
            this.tours.clear();
            this.tours.addAll(tours);
            notifyDataSetChanged();
        }
        Log.d("mi505", "tours.size() from setTours: " + tours.size());
    }

    @Override
    public int getItemCount() {

        Log.d("mi505", "tours.size() from getItemCount: " + tours.size());

        return tours.size();
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        return false;
    }

    private void remove(int position) {
        Tour removedTour = tours.get(position);
        tours.remove(position);
        notifyItemRemoved(position);

        FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                .child("Notifications").child(removedTour.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(fragment.getContext(), "Tour has been removed successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(tours, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }

    public List<Tour> getTours() {
        return tours;
    }
}
