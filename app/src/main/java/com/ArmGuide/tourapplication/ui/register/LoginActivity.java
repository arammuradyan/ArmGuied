package com.ArmGuide.tourapplication.ui.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ArmGuide.tourapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class LoginActivity extends Fragment {

    private RecyclerView infoRecyclerView;
    private FloatingActionButton addInfo;

    private MyRecyclerViewAdapter adapter;

    public static final int GET_INFO_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        infoRecyclerView = view.findViewById(R.id.recyclerView_id);
//        addInfo = view.findViewById(R.id.addInfo_id);
        registerForContextMenu(addInfo);

        adapter = new MyRecyclerViewAdapter(getContext());
        infoRecyclerView.setAdapter(adapter);

        addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSecondActivity();
            }
        });
    }


    private void startSecondActivity() {
//        Intent intent = new Intent(getActivity(), SecondActyivity.class);
//        startActivityForResult(intent, GET_INFO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_INFO_REQUEST_CODE) {
//                String nameInfo = data.getStringExtra(SecondActyivity.NAME_DATA);
//                String surnameInfo = data.getStringExtra(SecondActyivity.SURNAME_DATA);
//                String imageInfo = data.getStringExtra(SecondActyivity.IMAGE_DATA);
//                String modeInfo = data.getStringExtra(SecondActyivity.MODE_DATA);

//                MyRecyclerViewAdapter.AdapterItemInfo
//                        itemInfo = new MyRecyclerViewAdapter.
//                        AdapterItemInfo(Uri.parse(imageInfo), nameInfo, surnameInfo, modeInfo);
//
//                adapter.notifyData(itemInfo);
            }
        }
    }
}
