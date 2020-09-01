package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {

    private String Check="";
    private TextView Title,QueTitle;
    private EditText FindMob,Que1,Que2,Que3;
    private Button VerifyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Check=getIntent().getStringExtra("check");
        //init
        Title=findViewById(R.id.title_of_page);
        FindMob=findViewById(R.id.find_mob_no);
        QueTitle=findViewById(R.id.textview_security);
        Que1=findViewById(R.id.que1);
        Que2=findViewById(R.id.que2);
        Que3=findViewById(R.id.que3);
        VerifyBtn=findViewById(R.id.verify_btn);

    }


    @Override
    protected void onStart() {

        super.onStart();

        FindMob.setVisibility(View.GONE);
        if (Check.equals("settings")){

            Title.setText("Set Questions ?");
            QueTitle.setText("Set the Answers of the following Security Questions");
            VerifyBtn.setText("Set Answers");

            //for getting the previous answer from the data base
            DisplayPreviousAnswer();

            VerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswer();
                }
            });
        }
        else if (Check.equals("login")){

            FindMob.setVisibility(View.VISIBLE);

            QueTitle.setText("Please Answer the Questions :");

            VerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyUser();
                }
            });
        }
    }

    private void VerifyUser() {
        final String Phone=FindMob.getText().toString();
        final String Question1=Que1.getText().toString().toLowerCase();
        final String Question2=Que2.getText().toString().toLowerCase();
        final String Question3=Que3.getText().toString().toLowerCase();

        if (Phone.equals("")){
            FindMob.setError("Phone Number must be entered");
            FindMob.setFocusable(true);
        }
        else if (Question1.equals("")){
            Que1.setError("Answer 1 is not entered");
            Que1.setFocusable(true);
        }
        else  if (Question2.equals("")){
            Que2.setError("Answer 2 is not entered");
            Que2.setFocusable(true);
        }
        else  if (Question3.equals("")){
            Que3.setError("Answer 3 is not entered");
            Que3.setFocusable(true);
        }
        else {
            final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Phone);

            Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //for getting the mobile no of forgot user
                        String Mphone = Objects.requireNonNull(dataSnapshot.child("Phone").getValue()).toString();

                        if (dataSnapshot.hasChild("Security Questions")) {
                            String ans1 = Objects.requireNonNull(dataSnapshot.child("Security Questions").child("Que1").getValue()).toString();
                            String ans2 = Objects.requireNonNull(dataSnapshot.child("Security Questions").child("Que2").getValue()).toString();
                            String ans3 = Objects.requireNonNull(dataSnapshot.child("Security Questions").child("Que3").getValue()).toString();

                            if (!ans1.equals(Question1)) {
                                Que1.setError("Answer 1 is not correct");
                                Que1.setFocusable(true);
                            }
                            else if (!ans2.equals(Question2)) {
                                Que2.setError("Answer 2 is not correct");
                                Que2.setFocusable(true);
                            }
                            else if (!ans3.equals(Question3)) {
                                Que3.setError("Answer 3 is not correct");
                                Que3.setFocusable(true);
                            }
                            else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                builder.setTitle("Set New Password :");

                                final EditText newpassword = new EditText(ForgetPasswordActivity.this);
                                newpassword.setHint("New Password");
                                builder.setView(newpassword);

                                builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        if (!newpassword.getText().toString().equals("")) {
                                            Ref.child("Password").setValue(newpassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ForgetPasswordActivity.this, "Password changed successfully...", Toast.LENGTH_SHORT).show();

                                                                Intent intent=new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ForgetPasswordActivity.this, "Error Occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.cancel();
                                    }
                                });


                                builder.show();
                            }
                        }
                        else {
                            Toast.makeText(ForgetPasswordActivity.this, "Security questions were not set.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ForgetPasswordActivity.this, "User not exists...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SetAnswer(){
        String Question1=Que1.getText().toString().toLowerCase();
        String Question2=Que2.getText().toString().toLowerCase();
        String Question3=Que3.getText().toString().toLowerCase();

        if (Question1.equals("")){
            Que1.setError("Answer must be filled!!!");
            Que1.setFocusable(true);
        }
        else if (Question2.equals("")){
            Que2.setError("Answer must be filled!!!");
            Que2.setFocusable(true);
        }
        else if (Question3.equals("")){
            Que3.setError("Answer must be filled!!!");
            Que3.setFocusable(true);
        }
        else {
            DatabaseReference Ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.CurrentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap=new HashMap<>();
            userdataMap.put("Que1",Question1);
            userdataMap.put("Que2",Question2);
            userdataMap.put("Que3",Question3);

            Ref.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetPasswordActivity.this, "Security Questions Updated..", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgetPasswordActivity.this, "Error Occur: "+e.getMessage()+"\n Try Again...", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void DisplayPreviousAnswer(){
        DatabaseReference Ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.CurrentOnlineUser.getPhone());

        Ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String ans1= Objects.requireNonNull(dataSnapshot.child("Que1").getValue()).toString();
                    String ans2= Objects.requireNonNull(dataSnapshot.child("Que2").getValue()).toString();
                    String ans3= Objects.requireNonNull(dataSnapshot.child("Que3").getValue()).toString();

                    Que1.setText(ans1);
                    Que2.setText(ans2);
                    Que3.setText(ans3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
