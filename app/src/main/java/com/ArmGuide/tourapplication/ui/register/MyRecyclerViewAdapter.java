package com.ArmGuide.tourapplication.ui.register;

import android.content.Context;
import android.net.Uri;
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

public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private List<AdapterItemInfo> listData = new ArrayList<>();

    public MyRecyclerViewAdapter(Context context) {
        this.context = context;
    }

//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
//        MyViewHolder holder = new MyViewHolder(view);
//        return holder;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!listData.isEmpty()) {
            AdapterItemInfo info = listData.get(position);
            holder.nameinfo.setText(info.nameInfo);
            holder.surnameInfo.setText(info.surnameInfo);
            holder.modeInfo.setText(info.modeInfo);
            holder.imageInfo.setImageURI(info.imageUri);
        }

    }

    public void notifyData(AdapterItemInfo item) {
        listData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageInfo;
        private TextView nameinfo;
        private TextView surnameInfo;
        private TextView modeInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageInfo = itemView.findViewById(R.id.infoImage);
//            nameinfo = itemView.findViewById(R.id.nameInfo);
//            surnameInfo = itemView.findViewById(R.id.surnName);
//            modeInfo = itemView.findViewById(R.id.modeInfo);
        }
    }

    static class AdapterItemInfo {
        private Uri imageUri;
        private String nameInfo;
        private String surnameInfo;
        private String modeInfo;

        public AdapterItemInfo(Uri imageUri, String nameInfo, String surnameInfo, String modeInfo) {
            this.imageUri = imageUri;
            this.nameInfo = nameInfo;
            this.surnameInfo = surnameInfo;
            this.modeInfo = modeInfo;
        }
    }
}
