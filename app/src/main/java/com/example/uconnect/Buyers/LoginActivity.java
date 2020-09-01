package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uconnect.Admin.AdminHomeActivity;
import com.example.uconnect.Seller.SellerCategoryActivity;
import com.example.uconnect.Model.Users;
import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button LoginBtn;
    private TextInputEditText lMobile, lPassword;
    private TextView lForgotPassword, lAdmin,lnotAdmin;
    private CheckBox lRememberme;
    private ProgressDialog loadingbar;
    private String parent_db_name = "Users";

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init
        LoginBtn = findViewById(R.id.login_page_login_btn);
        lMobile = findViewById(R.id.login_input_mobile_et);
        lPassword = findViewById(R.id.login_input_password_et);
        lForgotPassword = findViewById(R.id.forgot_password);
        lAdmin = findViewById(R.id.for_admin);
        lnotAdmin=findViewById(R.id.not_admin);
        lRememberme = findViewById(R.id.remember_me);
        loadingbar = new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        lAdmin.setVisibility(View.VISIBLE);

        Paper.init(this);


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });

        lAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBtn.setText("Admin Login");
                lAdmin.setVisibility(View.GONE);
                lnotAdmin.setVisibility(View.VISIBLE);

                parent_db_name="Admins";

            }
        });
        lnotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBtn.setText("Login");
                lAdmin.setVisibility(View.VISIBLE);
                lnotAdmin.setVisibility(View.GONE);

                parent_db_name="Users";

            }
        });

        lForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
    }

    private void loginuser() {

        String Phone = Objects.requireNonNull(lMobile.getText()).toString().trim();
        String Password = Objects.requireNonNull(lPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(Phone)) {
            lMobile.setError("Enter Mobile No.");
            lMobile.setFocusable(true);
        } else if (TextUtils.isEmpty(Password)) {
            lPassword.setError("Enter Password");
            lPassword.setFocusable(true);
        } else {
            loadingbar.setTitle("Logging...");
            loadingbar.setMessage("please wait, while we are checking details.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            Allowaccesstoaccount(Phone, Password);

        }
    }

    private void Allowaccesstoaccount(final String phone, final String password) {

        if(lRememberme.isChecked()){
            Paper.book().write(Prevelent.UserPhoneKey,phone);
            Paper.book().write(Prevelent.UserPasswordKey,password);
        }
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parent_db_name).child(phone).exists()) {

                    Users users = dataSnapshot.child(parent_db_name).child(phone).getValue(Users.class);

                    if (users.getPhone().equals(phone)) {

                        if (users.getPassword().equals(password)) {
                           if(parent_db_name.equals("Admins")){
                               Toast.makeText(LoginActivity.this, "Admin Logged in Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();

                               Intent intent=new Intent(LoginActivity.this, AdminHomeActivity.class);
                               startActivity(intent);
                               finish();
                           }
                           else if(parent_db_name.equals("Users")){
                               Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();

                               Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                               Prevelent.CurrentOnlineUser=users;
                               startActivity(intent);
                               finish();
                           }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Password is incorrect!!!", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with " + phone + " number do not exist!!!", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
