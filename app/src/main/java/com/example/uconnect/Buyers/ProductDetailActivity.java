package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.uconnect.Model.Products;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private Button AddtoCart, BuyNow;
    private ImageView ProductImagePd;
    private ElegantNumberButton QuantityCount;
    private TextView ProductNamePd, ProductDescriptionPd, ProductPricePd;
    private String ProductId = "",status="normal";
    private String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //init
        AddtoCart = findViewById(R.id.add_to_cart_btn);
        BuyNow = findViewById(R.id.buy_btn);
        ProductImagePd = findViewById(R.id.product_detail_image_pd);
        QuantityCount = findViewById(R.id.product_quantity_count_btn);
        ProductNamePd = findViewById(R.id.product_name_in_pd);
        ProductDescriptionPd = findViewById(R.id.product_description_in_pd);
        ProductPricePd = findViewById(R.id.product_price_in_pd);

        //getting the product id from home activity clicked image
        ProductId = getIntent().getStringExtra("pid");
        //fetching details
        getProductDetails(ProductId);


       AddtoCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (status.equals("order placed")|| status.equals("order shipped")){
                   Toast.makeText(ProductDetailActivity.this, "you can add more products. once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                   finish();
               }
               else {
                   addtocartlist();
               }
           }
       });

    }

    @Override
    protected void onStart() {

        super.onStart();
        CheckOrderState();
    }

    private void addtocartlist() {
        String SaveCurrentTime,SaveCurrentDate;

        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        SaveCurrentDate=currentDate.format(calfordate.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime=currenttime.format(calfordate.getTime());

        final DatabaseReference cartlistref=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartmap=new HashMap<>();
        cartmap.put("pid",ProductId);
        cartmap.put("PName",ProductNamePd.getText().toString());
        cartmap.put("PPrice",ProductPricePd.getText().toString());
        cartmap.put("Date",SaveCurrentDate);
        cartmap.put("Time",SaveCurrentTime);
        cartmap.put("Quantity",QuantityCount.getNumber());
        cartmap.put("Discount","");


        cartlistref.child("User View").child(Prevelent.CurrentOnlineUser.getPhone()).child("Products")
                .child(ProductId)
                .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//we done this because when user view is created then  on complete  we have to toast the message
                //so we try to make one more view that is admin view.
                if(task.isSuccessful()){

                    cartlistref.child("Admin View").child(Prevelent.CurrentOnlineUser.getPhone()).child("Products")
                            .child(ProductId)
                            .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailActivity.this, " Item Added to Cart...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductDetails(final String productId) {

        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        productReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    ProductNamePd.setText(products.getPname());
                    ProductDescriptionPd.setText(products.getDescription());
                    ProductPricePd.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).placeholder(R.drawable.logo1).into(ProductImagePd);

                    temp=products.getPrice();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState(){
        DatabaseReference OrderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.CurrentOnlineUser.getPhone());

        OrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String ShippingStatus=dataSnapshot.child("Status").getValue().toString();

                    if (ShippingStatus.equals("shipped")){
                        status="order shipped";
                    }
                    else  if (ShippingStatus.equals("not shipped")){

                        status="order placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
