package com.ArmGuide.tourapplication.ui.registr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ArmGuide.tourapplication.MainActivity;
import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Tour;
import com.ArmGuide.tourapplication.models.Tourist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

import static android.app.Activity.RESULT_OK;
import static com.ArmGuide.tourapplication.Constants.TOURISTS_AVATARS_STORAGE;
import static com.ArmGuide.tourapplication.Constants.TOURISTS_DATABASE_REFERENCE;

public class RegistAsTouristFragment extends Fragment {

    // Edit texts
    private EditText full_name_et, email_et, password_et,
            confirm_password_et,phonenumber_et;
    // Imageview
    private ImageView avatar_img;
    // Progres bar
    private ProgressBar tourist_pb;
    // Button
    private Button sign_up_btn;
    // Constans
    private static final int STORAGE_READ_REQUEST_CODE=98;
    private static final int IMAGE_URI_REQUEST_CODE=178;



    // Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAoutStateListener;
    private StorageTask mUploadTask;
    private String question;

    // Gallery uri
    private String avatarUri="";

    // Edit texts input
    private String name;
    private String email;
    private String password;
    private String phone;
    private String confirm_password;

    private SharedPreferences sharedPreferences;

    String[] data = {"The name of your pet?", "Mother's Maiden Name?", "jhgjhgj", "four", "five"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_regist_as_tourist, container, false);

        initAuth();
        vieiwInit(view);
        setOnClickListeners();
        sharedPreferences = getActivity().getSharedPreferences("statePref",0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                question = ((String) parent.getAdapter().getItem(position));
                Toast.makeText(getActivity().getBaseContext(), "Position = " + position + " " +
                        parent.getAdapter().getItem(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAoutStateListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAoutStateListener);
    }

    private void initAuth() {
    mAuth=FirebaseAuth.getInstance();
    mAoutStateListener=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
         if(firebaseAuth.getCurrentUser()!=null&& getActivity()!=null){
            // Intent intent= new Intent(getActivity(), MainActivity.class);
             //startActivity(intent);
             getActivity().finish();
         }
        }
    };
    }

    private void vieiwInit(View view) {
        // Edit text
        full_name_et=view.findViewById(R.id.full_name_et);
        email_et=view.findViewById(R.id.tourist_email_et);
        password_et=view.findViewById(R.id.tourist_password_et);
        confirm_password_et=view.findViewById(R.id.tourist_conifirm_password_et);
        phonenumber_et=view.findViewById(R.id.tourist_phoneNumber_et);

        //PB
        tourist_pb=view.findViewById(R.id.tourist_pb);

        // Button
        sign_up_btn=view.findViewById(R.id.tourist_signup_btn);

        // Imageview
        avatar_img=view.findViewById(R.id.tourist_profileImage_img);
    }

    private void setOnClickListeners() {

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             register();
                sharedPreferences.edit().putString("newState","newState").apply();
            }
        });

        avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             askPermission();
            }
        });
    }

    private void askPermission() {
        if(getActivity()!=null) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_READ_REQUEST_CODE);
            }else {
                startGalleryActivity();
            }
        }
    }
    private void startGalleryActivity() {
        Intent uriIntent=new Intent(Intent.ACTION_PICK);
        uriIntent.setType("image/*");
        startActivityForResult(uriIntent,IMAGE_URI_REQUEST_CODE);
        CustomIntent.customType(getActivity(),"fadein-to-fadeout");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_READ_REQUEST_CODE){
            for (int i = 0; i <permissions.length ; i++) {
                if ( permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    startGalleryActivity();
                }else {
                  Toast.makeText(getActivity(),"Permission denied can't load image",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_URI_REQUEST_CODE && resultCode == RESULT_OK) {
           if(data.getData()!=null){
            avatarUri=data.getData().toString();
               Picasso.get().load(avatarUri)
                       .placeholder(R.mipmap.ic_launcher)
                       .fit()
                       .centerCrop()
                       .into(avatar_img);
           }else{
               Toast.makeText(getActivity(),"EROR: Unknown",Toast.LENGTH_SHORT).show();
           }
        }
    }

    private void register(){

         name = full_name_et.getText().toString().trim();
         email = email_et.getText().toString().trim();
         password = password_et.getText().toString().trim();
         phone = phonenumber_et.getText().toString().trim();
         confirm_password = confirm_password_et.getText().toString().trim();

                         //0     1      2       3         4
          if(checkInputs(name,email,password,phone,confirm_password)){
              tourist_pb.setVisibility(ProgressBar.VISIBLE);

              mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){

                          if(mUploadTask!=null && mUploadTask.isInProgress()){
                              Toast.makeText(getContext(),"registration alreade in proces",Toast.LENGTH_SHORT).show();
                          }else{

                              if(!avatarUri.isEmpty()){
                                  uploadAvatar();
                              }else{
                                  saveTouristInDatabase(Uri.parse(avatarUri));

                              }
                          }
                      }else{
                          if(task.getException()!=null)
                          Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
    }

    private boolean checkInputs(String ... inputs){
        if(TextUtils.isEmpty(inputs[0])){
            full_name_et.setError("name is required");
            full_name_et.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(inputs[1])){
            email_et.setError("email is required");
            email_et.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(inputs[2])){
            password_et.setError("password is required");
            password_et.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(inputs[3])){
            phonenumber_et.setError("phone is required");
            phonenumber_et.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(inputs[4])){
            confirm_password_et.setError("conformation is required");
            confirm_password_et.requestFocus();
            return false;
        }

        if(!inputs[2].equals(inputs[4])){
            confirm_password_et.setError("wrong conformation"+"\n"+" please enter corect password");
            confirm_password_et.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(inputs[1]).matches()){
            email_et.setError("enter valid email");
            email_et.requestFocus();
            return false;
        }

        if(inputs[2].length()<6){
            password_et.setError("password shuld be atleast 6 characters long");
            password_et.requestFocus();
            return false;
        }
       return true;
    }

    private String getFileExtension(Uri uri){

       if(!uri.equals(Uri.EMPTY)){
        if(getActivity()!=null){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
        }
        return "";

       }
      return null;
    }


    private void uploadAvatar(){
       Uri uri=Uri.parse(avatarUri);


        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(TOURISTS_AVATARS_STORAGE);

        StorageReference imageReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));

       mUploadTask = imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

           @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null){

                   Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                   result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                        saveTouristInDatabase(uri);
                       }
                   });
               }
           }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Image upload feilure: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }

    private void saveTouristInDatabase(Uri uri){
        String id;

        if(mAuth.getCurrentUser()!=null)
        {
            id=mAuth.getCurrentUser().getUid();
        }
        else{
            id="";
            Toast.makeText(getContext(), "saveToristInDatabase: currentUser is null", Toast.LENGTH_SHORT).show();
        }

        Tourist currentTourist= getCurrentTourist(id,uri.toString());

        DatabaseReference touristsDatabaseReference=
                FirebaseDatabase.getInstance().getReference(TOURISTS_DATABASE_REFERENCE);
        // Reference for cuurent user by Auth - Uid
        touristsDatabaseReference.child(id).setValue(currentTourist).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                tourist_pb.setVisibility(ProgressBar.GONE);

            }
        });
    }

    private Tourist getCurrentTourist(String id, String uri){

        Tourist currentTourist= new Tourist();
        Tour tour1=new Tour();


        ArrayList<Tour> tours=new ArrayList<>();
        tours.add(tour1);

        currentTourist.setId(id);
        currentTourist.setEmail(email);
        currentTourist.setPassword(password);
        currentTourist.setFullName(name);
        currentTourist.setAvatarUrl(uri);
        currentTourist.setPhoneNumber(phone);
        currentTourist.setIsCompany(false);
        currentTourist.setTours(tours);
        return currentTourist;
    }
}
