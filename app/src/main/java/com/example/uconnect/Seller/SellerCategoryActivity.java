package com.example.uconnect.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.uconnect.Admin.AdminNewOrderActivity;
import com.example.uconnect.Buyers.HomeActivity;
import com.example.uconnect.Buyers.MainActivity;
import com.example.uconnect.R;

public class SellerCategoryActivity extends AppCompatActivity {

    private ImageView simpleTshirts,sportsTshirts,FemaleDressess,Sweaters;
    private ImageView Glass,Purses,Hats,Shoes;
    private ImageView Headphones,Watches,Laptops,MobilePhones;
    private Button CheckOrdersBtn,ModifyOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);

        simpleTshirts=findViewById(R.id.t_shirt);
        sportsTshirts=findViewById(R.id.sports_t_shirt);
        FemaleDressess=findViewById(R.id.female_dress);
        Sweaters=findViewById(R.id.sweaters);
        Glass=findViewById(R.id.glasses);
        Purses=findViewById(R.id.purse_bag);
        Hats=findViewById(R.id.hats);
        Shoes=findViewById(R.id.shoes);
        Headphones=findViewById(R.id.headphones);
        Watches=findViewById(R.id.watches);
        Laptops=findViewById(R.id.laptops);
        MobilePhones=findViewById(R.id.mobiles);
        CheckOrdersBtn=findViewById(R.id.check_new_order_btn);
        ModifyOrderBtn=findViewById(R.id.update_order_details_btn);

        ModifyOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

//        LogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(SellerCategoryActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });


        simpleTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Tshirts");
                startActivity(intent);

            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sports Tshirts");
                startActivity(intent);

            }
        });
        FemaleDressess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Female Dress");
                startActivity(intent);

            }
        });

       Sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);

            }
        });

        Glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);

            }
        });

        Purses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Purses & Bags");
                startActivity(intent);

            }
        });

        Hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);

            }
        });

        Shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);

            }
        });

        Headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","HeadPhones & Earphones");
                startActivity(intent);

            }
        });

        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);

            }
        });

        Watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);

            }
        });

        MobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Mobiles");
                startActivity(intent);

            }
        });

    }
}
