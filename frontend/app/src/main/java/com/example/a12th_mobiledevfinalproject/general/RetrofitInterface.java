package com.example.a12th_mobiledevfinalproject.general;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    // post requests login response
    @POST("/login")
    Call<Void> executeLogin(@Body HashMap<String, String> map);

    // post requests the server to verify the given login credentials
    @POST("/checkFirstTimeLogin")
    Call<Void> executeCheckFirstTimeLogin(@Body HashMap<String, String> map);

    // post requests logout response
    @POST("/logout")
    Call<Void> executeLogout(@Body HashMap<String, String> map);

    // post requests user's grades.
    @POST("/grade")
    Call<JsonObject> executeGrade(@Body HashMap<String, String> map);

    // post requests user's work data like classwork, projects, tests.
    @POST("/list")
    Call<JsonObject> executeList(@Body HashMap<String, String> map);

    // post requests school activities like holidays or events like standardized testing.
    @POST("/event")
    Call<JsonObject> executeEvents(@Body HashMap<String, String> map);

    // post requests user's schedule for each days.
    @POST("/schedule")
    Call<JsonObject> executeSchedule(@Body HashMap<String, String> map);

    // post requests such as user's name, id, locker info.
    @POST("/profile")
    Call<JsonObject> executeProfile(@Body HashMap<String, String> map);
}
