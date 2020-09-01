package com.example.uconnect.Seller.ui.logout;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uconnect.Buyers.LoginActivity;
import com.example.uconnect.Buyers.MainActivity;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}