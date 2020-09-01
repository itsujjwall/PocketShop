package com.example.uconnect.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.HashMap;

public class AdminModifyProductsActivity extends AppCompatActivity {
    private Button ApplyChangesBtn,DeleteProductBtn;
    private EditText EditProductName,EditProductPrice,EditProductDescription;
    private ImageView UpdateImage;
    private String ProductId = "";
    private DatabaseReference ProductRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_products);

        ProductId = getIntent().getStringExtra("pid");

        ApplyChangesBtn=findViewById(R.id.modify_btnn);
        DeleteProductBtn=findViewById(R.id.delete_product_btnn);
        EditProductName=findViewById(R.id.modify_input_name);
        EditProductDescription=findViewById(R.id.modify_description);
        EditProductPrice=findViewById(R.id.modify_input_price);
        UpdateImage=findViewById(R.id.modify_image);

        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products").child(ProductId);


        DisplaySpecificProduct();

        ApplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychanges();
            }
        });

        DeleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });

    }

    private void deleteThisProduct() {
        ProductRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AdminModifyProductsActivity.this, "Product Deleted Successfully...", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminModifyProductsActivity.this, "Error Occur: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void applychanges() {
        String PName=EditProductName.getText().toString();
        String PDesc=EditProductDescription.getText().toString();
        String PPrice=EditProductPrice.getText().toString();

        if (PName.equals("")){
            EditProductName.setError("Product Name must be filled!!!");
            EditProductName.setFocusable(true);
        }
        else if (PDesc.equals("")){
            EditProductDescription.setError("Product Description must be filled!!!");
            EditProductDescription.setFocusable(true);
        }
        else if (PPrice.equals("")){
            EditProductPrice.setError("Product Price must be filled!!!");
            EditProductPrice.setFocusable(true);
        }
        else {

            HashMap<String,Object> Productmap=new HashMap<>();
            Productmap.put("pid",ProductId);
            Productmap.put("Description",PDesc);
            //Productmap.put("Image",UpdateImage);
            Productmap.put("Price",PPrice);
            Productmap.put("Pname",PName);

            ProductRef.updateChildren(Productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminModifyProductsActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminModifyProductsActivity.this, "Error Occur: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void DisplaySpecificProduct() {

        ProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   String pname=dataSnapshot.child("Pname").getValue().toString();
                   String pprice=dataSnapshot.child("Price").getValue().toString();
                   String pdescription=dataSnapshot.child("Description").getValue().toString();
                   String pimage=dataSnapshot.child("Image").getValue().toString();

                   EditProductName.setText(pname);
                   EditProductDescription.setText(pdescription);
                   EditProductPrice.setText(pprice);
                    Picasso.get().load(pimage).placeholder(R.drawable.applogo).into(UpdateImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
