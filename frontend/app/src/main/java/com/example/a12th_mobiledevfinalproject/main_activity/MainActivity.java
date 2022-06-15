package com.example.a12th_mobiledevfinalproject.main_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.home.Home;
import com.example.a12th_mobiledevfinalproject.loading.LoadingDialog;
import com.example.a12th_mobiledevfinalproject.log_in.LogIn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new App(this);
    }
}