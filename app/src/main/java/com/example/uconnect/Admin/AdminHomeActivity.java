package com.example.uconnect.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uconnect.Buyers.HomeActivity;
import com.example.uconnect.Buyers.MainActivity;
import com.example.uconnect.R;
import com.example.uconnect.Seller.SellerCategoryActivity;

public class AdminHomeActivity extends AppCompatActivity {
    private Button LogoutBtn,CheckOrdersBtn,ModifyOrderBtn,ApproveNewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        LogoutBtn=findViewById(R.id.logout_btn);
        CheckOrdersBtn=findViewById(R.id.check_new_order_btn);
        ModifyOrderBtn=findViewById(R.id.update_order_details_btn);
        ApproveNewOrders=findViewById(R.id.approve_order_btn);

        ModifyOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        ApproveNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminCheckNewProductActivity.class);
                startActivity(intent);
            }
        });
    }
}
