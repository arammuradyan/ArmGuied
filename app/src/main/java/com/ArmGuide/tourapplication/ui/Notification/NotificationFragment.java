package com.ArmGuide.tourapplication.ui.Notification;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.basel.FadedRecyclerView.FadedRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    private FadedRecyclerView recyclerViewNotification;
    private AdapterRecyclerNotification adapterRecyclerNotification;
    private NotificationsViewModel notificationsViewModel;
    private String userKey;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsViewModel = ViewModelProviders.of(NotificationFragment.this).get(NotificationsViewModel.class);

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerViewNotification = view.findViewById(R.id.recyclerNotif);
        recyclerViewNotification.setHasFixedSize(true);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        adapterRecyclerNotification = new AdapterRecyclerNotification(NotificationFragment.this, userKey);
        recyclerViewNotification.setAdapter(adapterRecyclerNotification);
        recyclerViewNotification.setMaskColor(android.R.color.black);
        recyclerViewNotification.setIsAggressive(false);

        // Setup onItemTouchHandler to enable drag and drop , swipe left or right
        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(adapterRecyclerNotification,
                true, true,
                true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewNotification);

        recyclerViewNotification.addItemDecoration(new RVHItemDividerDecoration(view.getContext(), LinearLayoutManager.VERTICAL));




        notificationsViewModel.getLiveData().observe(NotificationFragment.this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(final List<Tour> tours) {
                if(tours.size()>0) {
                    adapterRecyclerNotification.setTours(tours);
                    adapterRecyclerNotification.notifyDataSetChanged();

                    /*recyclerViewNotification.addOnItemTouchListener(new RVHItemClickListener(getContext(), new RVHItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Toast.makeText(view.getContext(), "Tour name: "+ adapterRecyclerNotification.getTours().get(position).getPlaceName(), Toast.LENGTH_SHORT).show();

                        }
                    }));*/
                }
            }
        });
    }
}
