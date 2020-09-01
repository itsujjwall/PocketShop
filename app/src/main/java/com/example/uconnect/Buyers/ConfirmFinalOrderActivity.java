package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private TextInputEditText SName,SPhone,SAddress,SPincode,SCity;
    private Button SConfirm;
    private String TotalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        //init

        SName=findViewById(R.id.shipping_name);
        SPhone=findViewById(R.id.shipping_phone);
        SAddress=findViewById(R.id.shipping_address);
        SPincode=findViewById(R.id.shipping_pincode);
        SCity=findViewById(R.id.shipping_city);
        SConfirm=findViewById(R.id.final_order_btn);

        TotalAmount=getIntent().getStringExtra("Total Price");


        SConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

    }

    private void check() {

        String name= Objects.requireNonNull(SName.getText()).toString().trim();
        String mob= Objects.requireNonNull(SPhone.getText()).toString().trim();
        String address= Objects.requireNonNull(SAddress.getText()).toString().trim();
        String city= Objects.requireNonNull(SCity.getText()).toString().trim();
        String pincode= Objects.requireNonNull(SPincode.getText()).toString().trim();

        if (TextUtils.isEmpty(Objects.requireNonNull(SName.getText()).toString())){

            SName.setError("Enter your Name");
            SName.setFocusable(true);
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(SPhone.getText()).toString())){

            SPhone.setError("Enter your Mobile no.");
            SPhone.setFocusable(true);
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(SCity.getText()).toString())){

            SCity.setError("Enter your City");
            SCity.setFocusable(true);
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(SAddress.getText()).toString())){

            SAddress.setError("Enter your Address");
            SAddress.setFocusable(true);
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(SPincode.getText()).toString())){

            SPincode.setError("Enter your Pincode");
            SPincode.setFocusable(true);
        }
        else if (mob.length()<10){

            SPhone.setError("Phone Number must be 10 digit only");
            SPhone.setFocusable(true);
        }
        else if(mob.length()>10)
        {
            SPhone.setError("Phone Number must be 10 digit only");
            SPhone.setFocusable(true);
        }
        else if (pincode.length()<6 ){

            SPincode.setError("Pin code must be 6 digit only");
            SPincode.setFocusable(true);
        }
        else if (pincode.length()>6){

            SPincode.setError("Pin code must be 6 digit only");
            SPincode.setFocusable(true);
        }
        else {
                ConfirmOrder();
        }

    }

    private void ConfirmOrder() {

        final String SaveCurrentTime,SaveCurrentDate;

        Calendar calfordate=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        SaveCurrentDate=currentDate.format(calfordate.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime=currenttime.format(calfordate.getTime());

        final DatabaseReference OrderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevelent.CurrentOnlineUser.getPhone());

        HashMap<String,Object> OrdersMap=new HashMap<>();
        OrdersMap.put("TotalAmount",TotalAmount);
        OrdersMap.put("Name", Objects.requireNonNull(SName.getText()).toString());
        OrdersMap.put("Phone", Objects.requireNonNull(SPhone.getText()).toString());
        OrdersMap.put("Address", Objects.requireNonNull(SAddress.getText()).toString());
        OrdersMap.put("City", Objects.requireNonNull(SCity.getText()).toString());
        OrdersMap.put("Pincode", Objects.requireNonNull(SPincode.getText()).toString());
        OrdersMap.put("Date",SaveCurrentDate);
        OrdersMap.put("Time",SaveCurrentTime);
        OrdersMap.put("Status","not shipped");

        OrderRef.updateChildren(OrdersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").
                            child(Prevelent.CurrentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully...", Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Please Try Again, "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfirmFinalOrderActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
