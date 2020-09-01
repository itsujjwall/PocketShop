package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccount;
    private TextInputEditText mName,mMobile,mPassword;
    //progress bar to display while registering user
    ProgressDialog loadingbar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init
        CreateAccount=findViewById(R.id.register_page_registerbtn);
        mName=findViewById(R.id.register_input_name_et);
        mMobile=findViewById(R.id.register_input_mobile_et);
        mPassword=findViewById(R.id.register_input_password_et);
        loadingbar=new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        databaseReference=FirebaseDatabase.getInstance().getReference();


        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String Name= Objects.requireNonNull(mName.getText()).toString().trim();
        String Phone= Objects.requireNonNull(mMobile.getText()).toString().trim();
        String Password= Objects.requireNonNull(mPassword.getText()).toString().trim();

        if(TextUtils.isEmpty(Name))
        {
            mName.setError("Enter Name");
            mName.setFocusable(true);
        }
        else  if(TextUtils.isEmpty(Phone))
        {
            mMobile.setError("Enter Mobile No.");
            mMobile.setFocusable(true);
        }
        else  if(TextUtils.isEmpty(Password))
        {
            mPassword.setError("Enter Password");
            mPassword.setFocusable(true);
        }
        else if(Phone.length()<10){
            mMobile.setError("Mobile no. contains 10 digits");
            mMobile.setFocusable(true);
        }
        else if(Phone.length()>10){
            mMobile.setError("Mobile no. contains 10 digits");
            mMobile.setFocusable(true);
        }
        else if(Password.length()<6){
            mPassword.setError("Password must be 6 character length.");
            mPassword.setFocusable(true);
        }
        else{
            validatePhone(Name,Phone,Password);
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("please wait, while we are checking credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

        }
    }

    private void validatePhone(final String name, final String phone, final String password) {

        final DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!(dataSnapshot.child("Users").child(phone).exists())){
                     HashMap<String, Object> userdataMap=new HashMap<>();
                     userdataMap.put("Name",name);
                     userdataMap.put("Phone",phone);
                     userdataMap.put("Password",password);

                     databaseReference.child("Users").child(phone).updateChildren(userdataMap)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         Toast.makeText(RegisterActivity.this, "Account Created Successfully...", Toast.LENGTH_SHORT).show();
                                         loadingbar.dismiss();

                                         Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                         startActivity(intent);
                                         finish();
                                     }
                                     else {
                                         loadingbar.dismiss();
                                         Toast.makeText(RegisterActivity.this, "Error Occur, Please try again...", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             loadingbar.dismiss();
                             Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });

                 }
                 else {
                     Toast.makeText(RegisterActivity.this, "This "+phone+" is already exist...", Toast.LENGTH_SHORT).show();
                     loadingbar.dismiss();
                     Toast.makeText(RegisterActivity.this, "Please try again using another Number", Toast.LENGTH_SHORT).show();

                     Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                     startActivity(intent);
                     finish();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
