package com.example.a12th_mobiledevfinalproject.user_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.a12th_mobiledevfinalproject.R;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        new App(this);
    }
}