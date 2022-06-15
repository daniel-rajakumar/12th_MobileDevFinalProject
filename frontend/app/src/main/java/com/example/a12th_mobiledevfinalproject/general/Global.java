package com.example.a12th_mobiledevfinalproject.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.IntentCompat;

import com.example.a12th_mobiledevfinalproject.home.Home;
import com.example.a12th_mobiledevfinalproject.log_in.LogIn;
import com.example.a12th_mobiledevfinalproject.main_activity.App;
import com.example.a12th_mobiledevfinalproject.main_activity.MainActivity;
import com.example.a12th_mobiledevfinalproject.user_profile.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Global {
    public static RetrofitInterface retrofitInterface;
    public static User current_user;
    public static Gson gson = new Gson();
    static SharedPreferences settings = App.mainActivity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    static SharedPreferences.Editor editor = settings.edit();


    // save the user object locally
    public static void saveUserLocally(){
        editor.putString("user", gson.toJson(Global.current_user));
        editor.apply();
    }

    // delete the user object locally
    public static void deleteUserLocally(){
        editor.remove("user");
        editor.apply();
    }

    // load user object from local cache.
    public static void loadPreferences() {
        Global.setUpRetroFit();
        String json = settings.getString("user", null);
        Global.current_user = gson.fromJson(json, User.class);
    }


    // setup retrofit
    private static void setUpRetroFit(){
        final String BASEURL = "http://" + "10.0.0.216" + ":8080/";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        Global.retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    // switch to home activity
    public static void activityToHome(){
        Intent intent = new Intent(App.mainActivity, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mainActivity.startActivity(intent);
    }

    // switch to profile activity
    public static void activityToUserProfile(){
        Intent intent = new Intent(App.mainActivity, UserProfile.class);
        App.mainActivity.startActivity(intent);
    }

    // switch to login activity
    public static void activityToLogin(){
        Intent intent = new Intent(App.mainActivity, LogIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mainActivity.startActivity(intent);
    }


    // check user's network status
    public static boolean isUserOnline(){
        final ConnectivityManager conMgr = (ConnectivityManager) App.mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }



    // format json string
    public static String prettifyJSON(String json){
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = parser.parse(String.valueOf(json));
        return gson.toJson(el);
    }

}

