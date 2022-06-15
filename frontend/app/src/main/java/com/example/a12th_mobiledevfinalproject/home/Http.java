package com.example.a12th_mobiledevfinalproject.home;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.main_activity.App;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Http {
    private static final String TAG = "test";
    public static boolean homeData = false, listData = false, eventData = false, scheduleData = false, profileData = false;

    // sends list post request to the server.
    public static void getListData(){
        Call<JsonObject> call = Global.retrofitInterface.executeList(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    Global.current_user.setList(response.body());
                    Global.saveUserLocally();
                }
                Log.d(TAG, response.code() + ": List | onResponse: " + response.body());
                listData = true;
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "List | onFailure: " + t);
            }
        });
    }

    // sends schedule post request to the server.
    public static void getScheduleData(){
        Call<JsonObject> call = Global.retrofitInterface.executeSchedule(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    Global.current_user.setSchedule(response.body());
                    Global.saveUserLocally();

                    if (response.body().get("data").getAsJsonArray().size() == 0) {
                        getScheduleData();
                        return;
                    }
                }
                Log.d(TAG, response.code() + ": Schedule | onResponse: " + response.body());



                scheduleData = true;
//                Log.d(TAG, "onResponse: " + response.body());

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "Schedule | onFailure: " + t);
            }
        });
    }

    // sends event post request to the server.
    public static void getEventData(){

        Call<JsonObject> call = Global.retrofitInterface.executeEvents(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                Log.d(TAG, response.code() + " daniel ");
                if (response.code() == 200) {
                    assert response.body() != null;
                    Global.current_user.setEvent(response.body());
                    Global.saveUserLocally();
                }
                Log.d(TAG, response.code() + " Event | onResponse: " + response.body());
                if (response.code() == 404){
                    getEventData();
                    return;
                }
                eventData = true;
//                Log.d(TAG, "onResponse: " + Global.current_user.getCalendar());
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "Event | onFailure: " + t);
            }
        });
    }

    // sends logout post request to the server.
    public static void logOut(){
        Call<Void> call = Global.retrofitInterface.executeLogout(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 200 || response.code() == 404) {
                    Global.current_user = null;
                    Global.deleteUserLocally();
                    Global.activityToLogin();
                    Toast.makeText(App.mainActivity, "Logged out, successfully.", Toast.LENGTH_SHORT).show();
                    reset();

                } else {
                    Toast.makeText(App.mainActivity, "Logging out, unsuccessful.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (Global.isUserOnline()){
                    Toast.makeText(App.mainActivity, "Logout failed. The server is down.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.mainActivity, "Logout failed. Connect to the internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // sends home post request to the server.
    public static void getHomeData(){
        Call<JsonObject> call = Global.retrofitInterface.executeGrade(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
//                    textView.setText(prettifyJSON(response.body().toString()));
                    Global.current_user.setGrade(response.body());
                    Global.saveUserLocally();
                }
                Log.d(TAG, response.code() + ": Home | onResponse: " + response.body());
                homeData = true;
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "Home | onFailure: " + t);
            }
        });
    }

    // sends profile post request to the server.
    public static void getProfileData(){
        Call<JsonObject> call = Global.retrofitInterface.executeProfile(new HashMap<String, String>(){{
            put("username", Global.current_user.getUsername());
            put("password", Global.current_user.getPassword());
        }});

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
//                    textView.setText(prettifyJSON(response.body().toString()));
                    Global.current_user.setProfile(response.body());
                    Global.saveUserLocally();
                }
                Log.d(TAG, response.code() + ": Profile | onResponse: " + response.body());
                if (response.code() == 404){
                    getProfileData();
                    return;
                }
                profileData = true;
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "Profile | onFailure: " + t);
            }
        });
    }

    // get ALL data from the server
    public static void getAllData(){
        if (Global.current_user.getProfile() == null){
            getProfileData();
        }
        if (Global.current_user.getGrade() == null){
            getHomeData();
        }
        if (Global.current_user.getList() == null){
            getListData();
        }
        if (Global.current_user.getEvents() == null){
            getEventData();
        }
        if (Global.current_user.getSchedule() == null){
            getScheduleData();
        }
    }

    public static void reset(){
        homeData = listData = eventData = scheduleData = profileData = false;
    }
}
