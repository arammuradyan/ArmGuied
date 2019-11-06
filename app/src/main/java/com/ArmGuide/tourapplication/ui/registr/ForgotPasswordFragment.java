package com.ArmGuide.tourapplication.ui.registr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    DialogFragment dialogFragmentQeustion;
    private Button next;
    private EditText edtEmail;

    final String LOG_TAG = "myLogs";
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, null);
        next = v.findViewById(R.id.next);
        next.setOnClickListener(this);
        edtEmail = v.findViewById(R.id.login_email_et);
        return v;
    }


    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        String email = edtEmail.getText().toString();
        sendPasswordResetEmail(email);

    }

    private void checkUserIsExist(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                    boolean isExistingUser = !task.getResult().getSignInMethods().isEmpty();
                    if (isExistingUser) {
                      //  QuestionFragment.show(getFragmentManager(), "dlg1");
                    } else {
                        Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_LONG).show();
                }
               // dismiss();
            }
        });
    }

    void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Please check your email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
