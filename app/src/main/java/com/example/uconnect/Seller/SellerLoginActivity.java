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

import java.util.Objects;

public class SellerLoginActivity extends AppCompatActivity {

    private Button SellerLoginBtnn, NotHaveAccountBtn;
    private TextInputEditText SellerLoginEmail, SellerLoginPassword;
    ProgressDialog loadingbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        loadingbar = new ProgressDialog(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        SellerLoginBtnn = findViewById(R.id.seller_Login_btn);
        NotHaveAccountBtn = findViewById(R.id.seller_Not_have_account_btn);
        SellerLoginEmail = findViewById(R.id.seller_login_email);
        SellerLoginPassword = findViewById(R.id.seller_login_password);
        mAuth = FirebaseAuth.getInstance();


        SellerLoginBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSeller();
            }
        });


        NotHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerLoginActivity.this,SellerRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void LoginSeller() {

        final String email = Objects.requireNonNull(SellerLoginEmail.getText()).toString();
        final String password = Objects.requireNonNull(SellerLoginPassword.getText()).toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.equals("")) {
            SellerLoginEmail.setError("Please enter Seller's Email");
            SellerLoginEmail.setFocusable(true);
        } else if (!email.matches(emailPattern)) {
            SellerLoginEmail.setError("Please enter valid Email");
            SellerLoginEmail.setFocusable(true);
        } else if (password.equals("")) {
            SellerLoginPassword.setError("Please enter Seller's Password");
            SellerLoginPassword.setFocusable(true);
        } else {

            loadingbar.setTitle("Logging Seller's Account");
            loadingbar.setMessage("please wait, while we were logging you...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loadingbar.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Seller's Login Successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerLoginActivity.this, SellerMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SellerLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SellerLoginActivity.this, "Error Occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            });


        }

    }

}

