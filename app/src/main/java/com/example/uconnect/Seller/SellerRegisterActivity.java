package com.example.uconnect.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SellerRegisterActivity extends AppCompatActivity {
    private Button SellerRegisterBtn,SellerLoginBtn;
    private TextInputEditText SName,SPhone,SEmail,SPassword,SAddress;
    private FirebaseAuth mAuth;

    ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        SellerLoginBtn=findViewById(R.id.seller_already_have_account_btn);
        SellerRegisterBtn=findViewById(R.id.seller_register_btn);
        SName=findViewById(R.id.seller_name);
        SPhone=findViewById(R.id.seller_phone);
        SEmail=findViewById(R.id.seller_email);
        SPassword=findViewById(R.id.seller_password);
        SAddress=findViewById(R.id.seller_address);
        loadingbar=new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        mAuth = FirebaseAuth.getInstance();


        SellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerRegisterActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        SellerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterSeller();
            }
        });
    }

    private void RegisterSeller() {
        final String name= Objects.requireNonNull(SName.getText()).toString();
        final String phone= Objects.requireNonNull(SPhone.getText()).toString();
        final String email= Objects.requireNonNull(SEmail.getText()).toString();
        final String password= Objects.requireNonNull(SPassword.getText()).toString();
        final String address= Objects.requireNonNull(SAddress.getText()).toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name.equals("")){
            SName.setError("Please enter Seller's Name");
            SName.setFocusable(true);
        }
        else if (phone.equals("")){
            SPhone.setError("Please enter Seller's Mobile Number");
            SPhone.setFocusable(true);
        }
        else if (phone.length()<10){
            SPhone.setError("Mobile number must be of 10 digits");
            SPhone.setFocusable(true);
        }
        else if (phone.length()>10){
            SPhone.setError("Mobile number must be of 10 digits");
            SPhone.setFocusable(true);
        }
        else if (email.equals("")){
            SEmail.setError("Please enter Seller's Email");
            SEmail.setFocusable(true);
        }
        else if (!email.matches(emailPattern)){
            SEmail.setError("Please enter valid Email");
            SEmail.setFocusable(true);
        }
        else if (password.equals("")){
            SPassword.setError("Please enter Seller's Password");
            SPassword.setFocusable(true);
        }
        else if (password.length()<6){
            SPassword.setError("password must be 6 character minimum");
            SPassword.setFocusable(true);
        }

        else if (address.equals("")){
            SAddress.setFocusable(true);
        }
        else {

            loadingbar.setTitle("Creating Seller's Account");
            loadingbar.setMessage("please wait, while we are checking credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final DatabaseReference reference= FirebaseDatabase.getInstance()
                                        .getReference();

                                String SID=mAuth.getCurrentUser().getUid();

                                HashMap<String ,Object> sellermap=new HashMap<>();
                                sellermap.put("Sid",SID);
                                sellermap.put("SName",name);
                                sellermap.put("SPhone",phone);
                                sellermap.put("SEmail",email);
                                sellermap.put("SPassword",password);
                                sellermap.put("SAddress",address);

                                reference.child("Sellers").child(SID).updateChildren(sellermap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    loadingbar.dismiss();
                                                    Toast.makeText(SellerRegisterActivity.this, "Seller's Account Created Successfully...", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(SellerRegisterActivity.this, SellerMainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SellerRegisterActivity.this, "Error Occur: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(SellerRegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                            }

                        }
                    });

        }

    }
}
