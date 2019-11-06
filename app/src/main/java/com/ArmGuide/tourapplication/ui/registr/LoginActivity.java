package com.ArmGuide.tourapplication.ui.registr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.tours.by.category.ToursByCategoryFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.onFragmentIteractionListener {

    DialogFragment dlg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_conteiner, new LoginFragment(),"loginFragment")
                .commit();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void openForgotPasswordScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_conteiner, new ForgotPasswordFragment(),"loginFragment")
                .commit();
    }
}
