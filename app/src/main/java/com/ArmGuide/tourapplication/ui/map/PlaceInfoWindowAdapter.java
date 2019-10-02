package com.ArmGuide.tourapplication.ui.map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ArmGuide.tourapplication.R;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Marker;


public class PlaceInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


   private final View window;
   private Context context;

    public PlaceInfoWindowAdapter(Context context) {
        this.context = context;
        window= LayoutInflater.from(context).inflate(R.layout.info_window,null);
    }
    private void infoWindowText(com.google.android.libraries.maps.model.Marker marker,View view){
        String title=marker.getTitle();
        TextView info_tv=view.findViewById(R.id.info_title_tv);
        if(!TextUtils.isEmpty(title)){
            info_tv.setText(title);
        }
        String info=marker.getSnippet();
        TextView description_tv=view.findViewById(R.id.info_description_tv);
        if(!TextUtils.isEmpty(info)){
            description_tv.setText(info);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        infoWindowText(marker,window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        infoWindowText(marker,window);
        return window;
    }


  /*  public PlaceInfoWindowAdapter(Context context) {
        this.context = context;
        window= LayoutInflater.from(context).inflate(R.layout.info_window,null);
    }

private void infoWindowText(com.google.android.libraries.maps.model.Marker marker,View view){
        String title=marker.getTitle();
    TextView info_tv=view.findViewById(R.id.info_title_tv);
    if(!TextUtils.isEmpty(title)){
        info_tv.setText(title);
    }
    String info=marker.getSnippet();
    TextView description_tv=view.findViewById(R.id.info_description_tv);
    if(!TextUtils.isEmpty(info)){
        description_tv.setText(info);
    }
}

    @Override
    public View getInfoWindow(Marker marker) {
        infoWindowText(marker,window);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        infoWindowText(marker,window);
        return null;
    }*/
}
