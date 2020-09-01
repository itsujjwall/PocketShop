package com.example.uconnect.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uconnect.Interface.ItemClickListner;
import com.example.uconnect.Model.Products;
import com.example.uconnect.R;
import com.example.uconnect.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference UnverifiedProducsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_product);

        UnverifiedProducsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.recycler_menu1);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(UnverifiedProducsRef.orderByChild("Verified").equalTo("No"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.TxtProductName.setText(model.getPname());
                holder.TxtProductPrice.setText("₹ "+model.getPrice());
                holder.TxtProductDescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.ImageofProduct);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String ProductId=model.getPid();

                        CharSequence[] options =new CharSequence[]{
                                "Yes",
                                "No",
                                "Delete"
                        };
                        final AlertDialog.Builder builder=new AlertDialog.Builder(AdminCheckNewProductActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        builder.setTitle("Want to Approved or Delete this Product :-");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which==0){

                                    ChangeProductStatus(ProductId);
                                }
                                if (which==2){
                                    DeleteProduct(ProductId);
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);

                return holder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    private void ChangeProductStatus(final String productId) {
        UnverifiedProducsRef.child(productId).child("Verified").setValue("yes").addOnCompleteListener
                (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminCheckNewProductActivity.this, productId+" \n Product is Verified...", Toast.LENGTH_SHORT).show();
//                            if (productId==null){
//                                finish();
//                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminCheckNewProductActivity.this, "Error Occur :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteProduct(final String productId) {

        UnverifiedProducsRef.child(productId).removeValue().addOnCompleteListener
                (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminCheckNewProductActivity.this, productId+" \n Product Deleted Successfully..", Toast.LENGTH_SHORT).show();
//                            if (productId==null){
//                                finish();
//                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminCheckNewProductActivity.this, "Error Occur :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
