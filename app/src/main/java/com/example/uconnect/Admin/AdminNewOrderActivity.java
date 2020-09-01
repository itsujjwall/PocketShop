package com.example.uconnect.Admin;

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

import com.example.uconnect.Model.AdminOrders;
import com.example.uconnect.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {
    private RecyclerView OrderList;
    private DatabaseReference OrdersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        OrdersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        OrderList=findViewById(R.id.Orders_list);
        OrderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(OrdersRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {

                holder.Username.setText("Name: "+model.getName());
                holder.UserPhone_no.setText("Mob. No.- "+model.getPhone());
                holder.UserAddress.setText("Address : "+model.getAddress()+"  "+model.getCity());
                holder.UserDateTime.setText("Ordered at: "+model.getDate()+"  "+model.getTime());
                holder.User_total_price.setText("Total Amount : ₹"+model.getTotalAmount());

                holder.Show_Orders_products.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Uid=getRef(position).getKey();
                        Intent intent=new Intent(AdminNewOrderActivity.this, AdminUserProductActivity.class);
                        intent.putExtra("uid",Uid);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                        "yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order product?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    String UID=getRef(position).getKey();

                                    RemoveOrder(UID);
                                }
                                else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        OrderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView Username,UserPhone_no,User_total_price,UserDateTime,UserAddress;
        public Button Show_Orders_products;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            UserAddress=itemView.findViewById(R.id.order_address_tv);
            User_total_price=itemView.findViewById(R.id.order_total_price_tv);
            Username=itemView.findViewById(R.id.order_user_name_tv);
            UserPhone_no=itemView.findViewById(R.id.order_phone_tv);
            UserDateTime=itemView.findViewById(R.id.order_date_time_tv);
            Show_Orders_products=itemView.findViewById(R.id.show_order_detail);

        }
    }

    private void RemoveOrder(String uid) {

         OrdersRef.child(uid).removeValue();
    }
}
