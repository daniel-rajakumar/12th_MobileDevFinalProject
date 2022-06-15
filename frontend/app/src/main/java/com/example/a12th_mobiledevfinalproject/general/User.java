package com.example.a12th_mobiledevfinalproject.general;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

public class User implements Cloneable {

    private String username = "";
    private String password = "";
    private boolean autoLogin = false, biometrics = false;
    private JsonObject grade, list, event, schedule, profile;

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public String getUsername() {
        return username.trim();
    }

    public String getPassword() {
        return password.trim();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JsonObject getSchedule() {
        return schedule;
    }

    public void setSchedule(JsonObject schedule) {
        this.schedule = schedule;
    }

    public JsonObject getGrade() {
        return grade;
    }

    public void setGrade(JsonObject grade) {
        this.grade = grade;
    }

    public JsonObject getList() {
        return list;
    }

    public void setList(JsonObject list) {
        this.list = list;
    }

    @NonNull
    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    public boolean isBiometrics() {
        return biometrics;
    }

    public void setBiometrics(boolean biometrics) {
        this.biometrics = biometrics;
    }

    public JsonObject getEvent() {
        return event;
    }

    public JsonObject getProfile() {
        return profile;
    }

    public void setProfile(JsonObject profile) {
        this.profile = profile;
    }

    public JsonObject getEvents() {
        return event;
    }

    public void setEvent(JsonObject event) {
        this.event = event;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", autoLogin=" + autoLogin +
                ", grade=" + grade +
                ", list=" + list +
                '}';
    }


}
