package com.example.a12th_mobiledevfinalproject.main_activity;

import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.loading.LoadingDialog;
import com.example.a12th_mobiledevfinalproject.log_in.LogIn;

import java.util.concurrent.Executor;

public class App {

    private static final String TAG = "test";
    public static MainActivity mainActivity;
    App(MainActivity mainActivity){

        App.mainActivity = mainActivity;
        setPolicies();
        Global.loadPreferences();

        if (Global.current_user == null){
            Global.activityToLogin();
        } else {
            showBio();
        }
    }

    void setPolicies(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    void showBio(){
        new Biometricer(mainActivity);
    }



}

