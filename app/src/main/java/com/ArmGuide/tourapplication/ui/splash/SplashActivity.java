package com.ArmGuide.tourapplication.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Place;
import com.ArmGuide.tourapplication.ui.home.HomeViewModel;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SplashActivity extends AppCompatActivity {

    private ImageView application_icon;
    private TextView loading_tv;
    private Handler handler=new Handler();
    private HomeViewModel homeViewModel;
    private ArrayList<Place> places=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


       homeViewModel= ViewModelProviders.of(this).get(HomeViewModel.class);

     homeViewModel.getLiveData().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> data) {
                places.addAll(data);
            }
        });

        setAnimation();

       MyRunnable runnable=new MyRunnable(2);
       new Thread(runnable).start();
    }


    private void setAnimation(){
        application_icon=findViewById(R.id.splash_icon_img);

        loading_tv=findViewById(R.id.loading_tv);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(loading_tv);
        YoYo.with(Techniques.FadeOut).duration(2000).delay(2000).playOn(loading_tv);
        YoYo.with(Techniques.FadeIn).duration(1000).delay(4000).playOn(loading_tv);
    }

    private void startMainActinity(){
        Intent intent=new Intent(SplashActivity.this, MainActivity.class);
        Bundle placiesBundle=new Bundle();
        placiesBundle.putSerializable("placiesList",places);
        intent.putExtra("placiesList",placiesBundle);
        startActivity(intent);
        CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
        finish();
    }

   private  class MyRunnable implements Runnable{
        int seconds;

        private MyRunnable(int seconds) {
            this.seconds = seconds;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(seconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
      handler.post(new Runnable() {
          @Override
          public void run() {
              startMainActinity();
          }
      });
        }
    }
}
