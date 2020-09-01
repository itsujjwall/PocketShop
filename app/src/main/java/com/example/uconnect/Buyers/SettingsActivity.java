package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView ProfileImageView, changeImageBtn;
    private EditText NameEditText, PhoneEditText, PasswordEditText, AddressEditText;
    TextView CloseBtn, SaveBtn;
    private Button SecurityQuestionBtn;

    private DatabaseReference databaseReference;
    private Uri imageuri;
    private String myurl ="";
    private StorageTask UploadTask;
    private StorageReference storageReferenceProfile;
    private String Checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //init
        storageReferenceProfile = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        ProfileImageView = findViewById(R.id.settings_profile_image);
        changeImageBtn = findViewById(R.id.profile_image_change_btn);
        NameEditText = findViewById(R.id.setting_name);
        PhoneEditText = findViewById(R.id.setting_phone);
        PasswordEditText = findViewById(R.id.setting_password);
        AddressEditText = findViewById(R.id.setting_address);
        CloseBtn = findViewById(R.id.close_tv_settings);
        SaveBtn = findViewById(R.id.save_tv_settings);
        SecurityQuestionBtn=findViewById(R.id.setting_security_btn);

        userInfoDisplay(ProfileImageView, NameEditText, PhoneEditText,PasswordEditText, AddressEditText);


        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.equals("clicked")) {
                    userInfoSave();
                } else {
                    UpdateonlyUser();
                }
            }
        });

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checker = "clicked";

                CropImage.activity(imageuri)
                        .setAspectRatio(1, 2 ).start(SettingsActivity.this);
            }
        });


        SecurityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();

            ProfileImageView.setImageURI(imageuri);

        } else {
            Toast.makeText(this, "Error Occur!!!, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void UpdateonlyUser() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Name", Objects.requireNonNull(NameEditText.getText()).toString());
        userMap.put("Password", Objects.requireNonNull(PasswordEditText.getText()).toString());
        userMap.put("Phone", PhoneEditText.getText().toString());
        userMap.put("Address", AddressEditText.getText().toString());
        reference1.child(Prevelent.CurrentOnlineUser.getPhone()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSave() {
        if (TextUtils.isEmpty(NameEditText.getText().toString())) {
            Toast.makeText(this, "Name is Compulsory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(AddressEditText.getText().toString())) {
            Toast.makeText(this, "Address is needed", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PhoneEditText.getText().toString())) {
            Toast.makeText(this, "Mobile No. is Compulsory...", Toast.LENGTH_SHORT).show();
        } else if (Checker.equals("clicked")) {
            UploadImage();
        }
    }

    private void UploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait, while we are updating yours details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageuri != null) {
            final StorageReference reference = storageReferenceProfile.
                    child(Prevelent.CurrentOnlineUser.getPhone() + "jpg");

            UploadTask = reference.putFile(imageuri);
            UploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadurl = task.getResult();
                        myurl = downloadurl.toString();

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("Name",NameEditText.getText().toString());
                        userMap.put("Password",PasswordEditText.getText().toString());
                        userMap.put("Phone", PhoneEditText.getText().toString());
                        userMap.put("Address", AddressEditText.getText().toString());
                        userMap.put("Image", myurl);
                        reference1.child(Prevelent.CurrentOnlineUser.getPhone()).updateChildren(userMap);
                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error Occur, Please Try again...", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    //for getting user information on settings page
    private void userInfoDisplay(final CircleImageView ProfileImageView, final EditText NameEditText, final EditText PhoneEditText,final EditText PasswordEditText, final EditText AddressEditText) {
        PhoneEditText.setText(Prevelent.CurrentOnlineUser.getPhone());
        PasswordEditText.setText(Prevelent.CurrentOnlineUser.getPassword());
        NameEditText.setText(Prevelent.CurrentOnlineUser.getName());
        AddressEditText.setText(Prevelent.CurrentOnlineUser.getAddress());
        Picasso.get().load(Prevelent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(ProfileImageView);
        }

}

