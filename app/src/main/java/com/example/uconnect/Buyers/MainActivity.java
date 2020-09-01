package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uconnect.Model.Users;
import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.example.uconnect.Seller.SellerLoginActivity;
import com.example.uconnect.Seller.SellerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button JoinNowBTN,LoginBTN;
    private TextView WantToSeller;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        JoinNowBTN=findViewById(R.id.main_page_join);
        LoginBTN=findViewById(R.id.main_page_login);
        WantToSeller=findViewById(R.id.want_to_seller);
        loadingbar=new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        Paper.init(this);

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        JoinNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        WantToSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey=Paper.book().read(Prevelent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevelent.UserPasswordKey);

        if(UserPhoneKey !="" && UserPasswordKey !=""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingbar.setTitle("Logging...");
                loadingbar.setMessage("please wait, while we are logging you...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this, SellerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {

                    Users users = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (users.getPhone().equals(phone)) {

                        if (users.getPassword().equals(password)) {

                                Toast.makeText(MainActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                                Prevelent.CurrentOnlineUser=users;
                                startActivity(intent);
                                finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
