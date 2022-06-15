package com.example.a12th_mobiledevfinalproject.log_in;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.general.User;
import com.example.a12th_mobiledevfinalproject.home.Http;
import com.example.a12th_mobiledevfinalproject.loading.LoadingDialog;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App {
    private static final String TAG = "test";
    LogIn logIn;

    Button button_login;
    EditText editText_userName, editText_password;
    static Switch switch_biometric;
    TextView textView_loginStatus;
    boolean isSavedUser;


     App(LogIn logIn){
         this.logIn = logIn;
         isSavedUser = Global.current_user != null;
         assign();
         setUps();
    }


    void setUps(){
        setUp_button();
        setUp_switch();
        setUp_textViewStatus();
        setUpEditText();
    }


    void assign(){
        button_login = logIn.findViewById(R.id.button_logIn);
        editText_userName = logIn.findViewById(R.id.editText_userName);
        editText_password = logIn.findViewById(R.id.editText_password);
        switch_biometric = logIn.findViewById(R.id.login_switch_biometric);
        textView_loginStatus = logIn.findViewById(R.id.textView_loginStatus);
    }

    void setUpEditText(){
        if (isSavedUser){
             editText_userName.setVisibility(View.GONE);
         } else {
             editText_userName.setVisibility(View.VISIBLE);
         }
    }
    void setUp_textViewStatus(){
        if (isSavedUser){
            textView_loginStatus.setVisibility(View.VISIBLE);
            textView_loginStatus.setText("Logged in as " + Global.current_user.getUsername());
        } else {
            textView_loginStatus.setVisibility(View.GONE);
        }
    }

    void setUp_switch() {
         if (isSavedUser){
             switch_biometric.setVisibility(View.GONE);
         } else {
             switch_biometric.setVisibility(View.VISIBLE);
         }

         switch_biometric.setOnCheckedChangeListener((compoundButton, b) -> {
             if (b)
                 Toast.makeText(logIn, "Biometrics on", Toast.LENGTH_SHORT).show();
             else
                 Toast.makeText(logIn, "Biometrics off", Toast.LENGTH_SHORT).show();
         });
    }

    // when the user presses login button...
    void setUp_button(){
        button_login.setOnClickListener(this::handleLogin);
    }


     // login based on network status
     void handleLogin(View v) {
         if (Global.isUserOnline()) {
             performOnlineLogin();
         } else {
             performOfflineLogin();
         }
    }


    // sends login when user is online
    void performOnlineLogin(){
        Log.d(TAG, "performOnlineLogin: " + Global.current_user);

        // if the user is new
        if (Global.current_user == null){
            button_login.setClickable(false);

            // starts with requesting to the backend...
            sendLoginCheckRequest();

        } else { // when user already exists, just validate their password and login (if biometric failed).
            String one = Global.current_user.getPassword().trim();
            String two = editText_password.getText().toString().trim();

            Log.d(TAG, "checkLocalStoredUser: " + String.format("[%s, %s]", one, two));
            if (one.equals(two)){
                Global.activityToHome();
            } else {
                Toast.makeText(logIn, "invalid password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // sends login when user is offline
    // if the user has been already logged in to their account, then log in, else prompt error message telling user to go online.
    void performOfflineLogin(){

        Log.d(TAG, "checkLocalStoredUser: " + Global.current_user);
        if (Global.current_user == null) {
            Toast.makeText(logIn, "Login fail. Please connect to internet.", Toast.LENGTH_SHORT).show();
            return;
        }

        String one = Global.current_user.getPassword().trim();
        String two = editText_password.getText().toString().trim();

        Log.d(TAG, "checkLocalStoredUser: " + String.format("[%s, %s]", one, two));
        if (one.equals(two)){
            Global.activityToHome();
        } else {
            Toast.makeText(logIn, "invalid password", Toast.LENGTH_SHORT).show();
        }
    }


    // sends login check post request to the server.
    void sendLoginCheckRequest(){
         // A call object that contains user's password and username in map dataset.
        Call<Void> call = Global.retrofitInterface.executeCheckFirstTimeLogin(new HashMap<String, String>(){{
            put("username", editText_userName.getText().toString().trim());
            put("password", editText_password.getText().toString().trim());
        }});

        // sent the call object to the server and wait for response back. This repose will tell either this account exists on the database.
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 200) { // when this is not their first time logging in.
                    Toast.makeText(logIn, "Welcome back! Please wait we are getting your data...", Toast.LENGTH_SHORT).show();

                    Global.current_user = new User();
                    Global.current_user.setUsername(editText_userName.getText().toString());
                    Global.current_user.setPassword(editText_password.getText().toString());

                    LoadingDialog.startLoadingPage(logIn);

                } else if (response.code() == 404) { // when this is the first time logging in.
                    Toast.makeText(logIn, "Signing in... Please wait, we are verifying you...", Toast.LENGTH_SHORT).show();
                    LoadingDialog.startLoadingPage(logIn);
                }

                sendLoginRequest();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                button_login.setClickable(true);
                Log.d(TAG, "onFailure: " + t);
                Toast.makeText(logIn, "Login fail. The server is down", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // sends login post request to the server.
    void sendLoginRequest(){
        Call<Void> call = Global.retrofitInterface.executeLogin(new HashMap<String, String>(){{
            put("username", editText_userName.getText().toString().trim());
            put("password", editText_password.getText().toString().trim());
        }});

        // this sends the login actions to the server.
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                LoadingDialog.dismissDialog();
                if (response.code() == 200) {
                    Toast.makeText(logIn, "Login success! Getting things ready for you...", Toast.LENGTH_SHORT).show();

                    // create an user object and store it locally for autologin.
                    Global.current_user = new User();
                    Global.current_user.setUsername(editText_userName.getText().toString());
                    Global.current_user.setPassword(editText_password.getText().toString());
                    Global.current_user.setAutoLogin(switch_biometric.isChecked());
                    Global.current_user.setBiometrics(switch_biometric.isChecked());
                    Global.saveUserLocally();

                    // erase user's information so when singing with different account.
                    editText_userName.setText("");
                    editText_password.setText("");

                    // DONE! switch to home page
                    goToHomePage();
                } else if (response.code() == 404) {
                    // if the username and password doesn't match
                    Toast.makeText(logIn, "Invalid login information. Please try again.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400){
                    // if the sever is not unavailable
                    Toast.makeText(logIn, "Server error! Please try again.", Toast.LENGTH_SHORT).show();
                }
                button_login.setClickable(true);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                LoadingDialog.dismissDialog();
                button_login.setClickable(true);
                // login failed for other reasons.
                Toast.makeText(logIn, "Login fail. ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // switches activity to home (main dashboard)
    void goToHomePage(){
        LoadingDialog.startLoadingPage(logIn);
        Http.getAllData();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: list: " + Http.listData + " | home: " + Http.homeData + " | event: " + Http.eventData + " | schedule: " + Http.scheduleData);

                if (Http.listData && Http.homeData && Http.eventData && Http.scheduleData) {
                    LoadingDialog.dismissDialog();
                    Global.activityToHome();
                    cancel();
                }

            }
        }, 0, 500);
    }

}
