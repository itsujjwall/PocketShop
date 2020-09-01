package com.example.uconnect.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uconnect.Model.Cart;
import com.example.uconnect.Prevelent.Prevelent;
import com.example.uconnect.R;
import com.example.uconnect.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    private Button NextButtonProcess;
    private TextView TotalPrice,CMessage;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int FinalTotalPrice=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init
        recyclerView=findViewById(R.id.cart_list_ca);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextButtonProcess=findViewById(R.id.next_btn_ca);
        TotalPrice=findViewById(R.id.total_price_ca);
        CMessage=findViewById(R.id.message_after_order_placed);

        TotalPrice.setText("Total Price : ₹"+(FinalTotalPrice));

        NextButtonProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TotalPrice.setText("Total Price : ₹"+(FinalTotalPrice));
                Toast.makeText(CartActivity.this, "Total Cost: "+FinalTotalPrice, Toast.LENGTH_LONG).show();

                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(FinalTotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

        CheckOrderState();
        final DatabaseReference cartlistRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartlistRef.child("User View")
                .child(Prevelent.CurrentOnlineUser.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                        holder.txtProductName.setText("Product Name : "+model.getPName());
                        holder.txtProductQuantity.setText("Quantity - "+model.getQuantity());
                        holder.txtProductPrice.setText(model.getPPrice());

                        //for calculating price of the product
                       int SingleProductQuantity=(Integer.parseInt(model.getQuantity()));
                       int SingleProductPrice=Integer.parseInt(model.getPPrice());
                       int SingleProductTotalPrice=(SingleProductPrice*SingleProductQuantity);
                        FinalTotalPrice+=SingleProductTotalPrice;

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0){
                                            Intent intent=new Intent(CartActivity.this, ProductDetailActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }
                                        if (which==1){
                                            cartlistRef.child("User View").child(Prevelent.CurrentOnlineUser.getPhone())
                                                    .child("Products").child(model.getPid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this, "Item Removed Successfully...", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_itms_layout,parent,false);
                        return new CartViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference OrderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.CurrentOnlineUser.getPhone());

        OrderRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()){
                  String ShippingStatus= Objects.requireNonNull(dataSnapshot.child("Status").getValue()).toString();
                  String UserName= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();

                  if (ShippingStatus.equals("shipped")){
                      TotalPrice.setText("Order Shipped Successfully...");
                        recyclerView.setVisibility(View.GONE);

                        CMessage.setVisibility(View.VISIBLE);
                        CMessage.setText("Congratulation, your order has been shipped Successfully.Soon You will received your order.");
                        NextButtonProcess.setVisibility(View.GONE);


                  }
                  else  if (ShippingStatus.equals("not shipped")){

                      TotalPrice.setText("Shipping Status - not shipped");
                      recyclerView.setVisibility(View.GONE);

                      CMessage.setVisibility(View.VISIBLE);
                      NextButtonProcess.setVisibility(View.GONE);
                  }

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
