package com.example.uconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class tempActivity extends AppCompatActivity {
    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        CategoryName=getIntent().getExtras().get("category").toString();
        Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
    }
}
