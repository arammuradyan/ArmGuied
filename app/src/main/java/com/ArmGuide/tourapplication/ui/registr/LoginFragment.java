package com.ArmGuide.tourapplication.ui.registr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
   //Buttons
   private Button login_btn;
    private TextView registr_as_tourist_btn;
    private TextView registr_as_company_btn;
    private TextView forgote_pass_btn;
    private ImageView eye;

    //Edit Texts
    private EditText login_password_et, login_email_et;

    //Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SharedPreferences sharedPreferences;
    Fragment dialogFragmentForgotePass;
    private onFragmentIteractionListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        viewInit(view);
        initFirebaseAuth();
        setButtonListeners();
        sharedPreferences = getActivity().getSharedPreferences("statePref", 0);
        dialogFragmentForgotePass = new ForgotPasswordFragment();

        return view;
    }

    interface onFragmentIteractionListener{
        void openForgotPasswordScreen();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eye = view.findViewById(R.id.iv_eyeView);

        eye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        login_password_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        break;
                    case MotionEvent.ACTION_UP:
                        login_password_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                login_password_et.setSelection(login_password_et.length());
                return false;
            }
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && getActivity() != null) {

                    //Intent intent=new Intent(getActivity(), MainActivity.class);
                    // startActivity(intent);
                    getActivity().finish();
                }
            }
        };
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && getActivity() != null) {
                    //Intent intent=new Intent(getActivity(), MainActivity.class);
                    // startActivity(intent);
                    getActivity().finish();
                }
            }
        };
    }

    private void viewInit(View view) {
        //Buttons
        login_btn = view.findViewById(R.id.log_in_btn);
        registr_as_tourist_btn = view.findViewById(R.id.regist_as_tourist_btn);
        registr_as_company_btn = view.findViewById(R.id.regist_as_company_btn);
        forgote_pass_btn=view.findViewById(R.id.forgote_pass);
        // Edit texts
        login_email_et = view.findViewById(R.id.login_email_et);
        login_password_et = view.findViewById(R.id.login_password_et);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onFragmentIteractionListener){
            listener = ((onFragmentIteractionListener) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(listener != null){
            listener = null;
        }
    }

    private void setButtonListeners() {

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                sharedPreferences.edit().putString("newState", "newState").apply();
                Log.d("MyLog", "LoginFragment " + sharedPreferences.getString("newState", "lll"));

            }
        });

        registr_as_company_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_conteiner, new RegistAsCompanyFragment())
                            .addToBackStack("loginstack")
                            .commit();
            }
        });

        registr_as_tourist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_conteiner, new RegistAsTouristFragment())
                            .addToBackStack("loginstack")
                            .commit();
            }
        });
        forgote_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openForgotPasswordScreen();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void login() {
        String email = login_email_et.getText().toString().trim();
        String password = login_password_et.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            login_email_et.setError("email is required");
            login_email_et.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            login_password_et.setError("password is required");
            login_password_et.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email_et.setError("enter valid email");
            login_email_et.requestFocus();
            return;
        }

        if (password.length() < 6) {
            login_password_et.setError("password shuld be atleast 6 characters long");
            login_password_et.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "sign in colback: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
            }
        });
    }


}