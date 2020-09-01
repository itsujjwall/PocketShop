package com.example.uconnect.Seller.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uconnect.R;
import com.example.uconnect.Seller.SellerCategoryActivity;


public class DashboardFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        Intent intent=new Intent(getContext(), SellerCategoryActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);


    }


}
