package com.example.uconnect.Seller;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uconnect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class SellerAddNewProductActivity extends AppCompatActivity {
    private TextInputEditText InputProductName, InputProductDescription, InputProductPrice;
    private ImageView InputProductImage;
    private Button AddNewProductButton;

    private String CategoryName, PName, PDescription, PPrice;
    private ProgressDialog loadingbar;
    private String SaveCurrentDate, SaveCurrentTime;
    private String ProductRandomKey;


    private static final int Galleryvar = 1;
    //for image storage purpose
    private StorageReference ProductImageRef;
    private String DownloadImageUrl;
    //for product storage purpose
    private DatabaseReference productReference,SellersRef;

    //for image
    private Uri Imageuri;


    private String SName,SEmail,SPhone,SAddress,Sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);


        CategoryName = getIntent().getExtras().get("category").toString();

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        SellersRef=FirebaseDatabase.getInstance().getReference().child("Sellers");

        //init
        AddNewProductButton = findViewById(R.id.add_new_product);
        InputProductName = findViewById(R.id.product_input_name);
        InputProductDescription = findViewById(R.id.product_input_description);
        InputProductPrice = findViewById(R.id.product_input_price);
        InputProductImage = findViewById(R.id.selet_product_image);
        loadingbar = new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

        //for getting sellers data reference from the firebase database
        SellersRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                           SName= Objects.requireNonNull(dataSnapshot.child("SName").getValue()).toString();
                            SEmail= Objects.requireNonNull(dataSnapshot.child("SEmail").getValue()).toString();
                            SPhone= Objects.requireNonNull(dataSnapshot.child("SPhone").getValue()).toString();
                            SAddress= Objects.requireNonNull(dataSnapshot.child("SAddress").getValue()).toString();
                            Sid= Objects.requireNonNull(dataSnapshot.child("Sid").getValue()).toString();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void openGallery() {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,Galleryvar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Galleryvar && resultCode==RESULT_OK && data!=null){

            Imageuri=data.getData();
            InputProductImage.setImageURI(Imageuri);

        }
    }

    private void validateProductData() {
        PDescription= Objects.requireNonNull(InputProductDescription.getText()).toString();
        PPrice= Objects.requireNonNull(InputProductPrice.getText()).toString();
        PName= Objects.requireNonNull(InputProductName.getText()).toString();

        if(Imageuri==null){
            Toast.makeText(this, "Image is not added!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(PDescription)){
            InputProductDescription.setError("Enter Product Description!!!");
            InputProductDescription.setFocusable(true);
        }
        else if(TextUtils.isEmpty(PName)){
            InputProductName.setError("Enter Product Name!!!");
            InputProductName.setFocusable(true);
        }
        else if(TextUtils.isEmpty(PPrice)){
            InputProductPrice.setError("Enter Product Price!!!");
            InputProductPrice.setFocusable(true);
        }


        else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        loadingbar.setMessage("please wait, While Product is Adding");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd,yyyy");
        SaveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime=currentTime.format(calendar.getTime());


        ProductRandomKey=SaveCurrentDate + SaveCurrentTime;

        final StorageReference filepath=ProductImageRef.child(Imageuri.getLastPathSegment()+ProductRandomKey+".jpg");

        final UploadTask uploadTask=filepath.putFile(Imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              String message=e.toString();

                Toast.makeText(SellerAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                //for getting image link
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        DownloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            DownloadImageUrl= task.getResult().toString();

                            Toast.makeText(SellerAddNewProductActivity.this, "Image url saved...", Toast.LENGTH_SHORT).show();

                            saveproductinfotodatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveproductinfotodatabase() {
        HashMap<String,Object> Productmap=new HashMap<>();
        Productmap.put("pid",ProductRandomKey);
        Productmap.put("Date",SaveCurrentDate);
        Productmap.put("Time",SaveCurrentTime);
        Productmap.put("Description",PDescription);
        Productmap.put("Image",DownloadImageUrl);//because image is stored in this url and we get the url in this variable
        Productmap.put("Category",CategoryName);
        Productmap.put("Price",PPrice);
        Productmap.put("Pname",PName);

        Productmap.put("SellerName",SName);
        Productmap.put("SellerAddress",SAddress);
        Productmap.put("SellerPhone",SPhone);
        Productmap.put("SellerEmail",SEmail);
        Productmap.put("SellerId",Sid);
        Productmap.put("Verified","No");

        productReference.child(ProductRandomKey).updateChildren(Productmap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          loadingbar.dismiss();
                          finish();


                          Toast.makeText(SellerAddNewProductActivity.this, "Product is added Successfully...", Toast.LENGTH_SHORT).show();
                      }
                      else {
                          loadingbar.dismiss();
                          String message=task.getException().toString();
                          Toast.makeText(SellerAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                      }
                    }
                });

    }



    }
