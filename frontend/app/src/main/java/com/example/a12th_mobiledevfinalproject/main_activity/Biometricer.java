package com.example.a12th_mobiledevfinalproject.main_activity;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.Home;
import com.example.a12th_mobiledevfinalproject.log_in.LogIn;

import java.util.concurrent.Executor;

public class Biometricer {
    private Executor executor;
    public BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;

    public Biometricer(MainActivity mainActivity) {
        Global.deleteUserLocally();
        assign();
        if (Global.current_user.isBiometrics()){
            ask();
        } else {
            Global.activityToHome();
        }
    }

    void assign() {
        executor = ContextCompat.getMainExecutor(App.mainActivity);
        biometricPrompt = new BiometricPrompt(App.mainActivity, executor, new authCallback());
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Genesis")
                .setSubtitle(Global.current_user.getUsername())
                .setNegativeButtonText("Cancel")
                .build();
    }

    void ask() {
        biometricPrompt.authenticate(promptInfo);
    }

}

class authCallback extends BiometricPrompt.AuthenticationCallback {
    private static final String TAG = "test";

    /**
     * Called when an unrecoverable error has been encountered and the operation is complete.
     * No further actions will be made on this object.
     *
     * @param errorCode An integer identifying the error message. The error message will usually
     *                  be one of the BIOMETRIC_ERROR constants.
     * @param errString A human-readable error string that can be shown on an UI
     */
    @Override
    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Log.d(TAG, "onAuthenticationError: ");
        Global.activityToLogin();
    }

    /**
     * Called when a biometric is recognized.
     *
     * @param result An object containing authentication-related data
     */
    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Log.d(TAG, "onAuthenticationSucceeded: ");
        Global.activityToHome();
    }

    /**
     * Called when a biometric is valid but not recognized.
     */
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }

}


