package com.ArmGuide.tourapplication.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import maes.tech.intentanim.CustomIntent;

public class SplashActivity extends AppCompatActivity {

    private ImageView application_icon;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setAnimation();

        MyRunnable runnable=new MyRunnable(5);
        new Thread(runnable).start();
    }

    private void setAnimation(){
        application_icon=findViewById(R.id.splash_icon_img);

        YoYo.with(Techniques.FadeIn).duration(2000).playOn(application_icon);
        YoYo.with(Techniques.FadeOut).duration(2000).delay(2000).playOn(application_icon);
        YoYo.with(Techniques.FadeIn).duration(1000).delay(4000).playOn(application_icon);
    }

    private void startMainActinity(){
        Intent intent=new Intent(SplashActivity.this, MainActivity.class);
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
                // Data enq stanum viewmodelic verjacneluc heto bacvuma MainActivity
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
