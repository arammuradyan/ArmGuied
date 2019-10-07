package com.ArmGuide.tourapplication.ui.registr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
   //Buttons
    private Button login_btn;
    private Button registr_as_tourist_btn;
    private Button registr_as_company_btn;

    //Edit Texts
    private EditText login_password_et, login_email_et;

    //Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        viewInit(view);
        initFirebaseAuth();
        setButtonListeners();

        return view;
    }

    private void initFirebaseAuth() {
   mAuth=FirebaseAuth.getInstance();

   mAuthStateListener=new FirebaseAuth.AuthStateListener() {
       @Override
       public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
          if(firebaseAuth.getCurrentUser()!=null && getActivity()!=null){
             //Intent intent=new Intent(getActivity(), MainActivity.class);
             // startActivity(intent);
              getActivity().finish();
          }
       }
   };
    }

    private void viewInit(View view){
        //Buttons
    login_btn=view.findViewById(R.id.log_in_btn);
    registr_as_tourist_btn = view.findViewById(R.id.regist_as_tourist_btn);
    registr_as_company_btn = view.findViewById(R.id.regist_as_company_btn);
       // Edit texts
    login_email_et=view.findViewById(R.id.login_email_et);
    login_password_et=view.findViewById(R.id.login_password_et);
}
private void setButtonListeners(){

        login_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login();
        }
    });

    registr_as_company_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null)
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_conteiner, new RegistAsCompanyFragment())
                            .addToBackStack("loginstack")
                            .commit();
            }
        });

    registr_as_tourist_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getActivity()!=null)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_conteiner, new RegistAsTouristFragment())
                    .addToBackStack("loginstack")
                    .commit();
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

    private void login(){
String email=login_email_et.getText().toString().trim();
String password=login_password_et.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            login_email_et.setError("email is required");
            login_email_et.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            login_password_et.setError("password is required");
            login_password_et.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            login_email_et.setError("enter valid email");
            login_email_et.requestFocus();
            return;
        }

        if(password.length()<6){
            login_password_et.setError("password shuld be atleast 6 characters long");
            login_password_et.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(getContext(),"sign in colback: "+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}