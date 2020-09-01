package com.example.uconnect.Seller.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uconnect.Buyers.MainActivity;
import com.example.uconnect.R;
import com.example.uconnect.Seller.SellerMainActivity;
import com.google.firebase.auth.FirebaseAuth;


public class LogoutFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseAuth mAuth;
        mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent=new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
