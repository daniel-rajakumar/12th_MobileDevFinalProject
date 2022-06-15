package com.example.a12th_mobiledevfinalproject.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.App;
import com.example.a12th_mobiledevfinalproject.home.Http;

import okio.Utf8;

public class Fragment_settings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
    }

    static class App {
        View view;
        TextView textView_name, textView_id;
        Button button;
        ImageView image;
        Switch aSwitch;
        private static final String TAG = "test";
        ConstraintLayout constraintLayout;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
//            textView = view.findViewById(R.id.fragment_textView_home);
            button = view.findViewById(R.id.fragment_button_signout);
            constraintLayout = view.findViewById(R.id.fragment_setting_profile_card);
            image = view.findViewById(R.id.fragment_imageView_user);
            textView_name = view.findViewById(R.id.fragment_textView_name);
            textView_id = view.findViewById(R.id.fragment_textView_id);
            aSwitch = view.findViewById(R.id.fragment_switch_biometric);
        }

        void setUp(){
            setUp_textView();
            setUp_button();
            setUP_cardViews();
            setUp_image();
            setUp_switches();
        }

        void setUp_switches(){
            aSwitch.setChecked(Global.current_user.isBiometrics());
            aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                Global.current_user.setBiometrics(b);
                Global.saveUserLocally();
                if (b)
                    Toast.makeText(com.example.a12th_mobiledevfinalproject.home.App.home, "Biometrics on", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(com.example.a12th_mobiledevfinalproject.home.App.home, "Biometrics off", Toast.LENGTH_SHORT).show();
            });
        }

        void setUp_image(){
            Global.current_user.getProfile().getAsJsonArray("data").forEach(e -> {
                Glide.with(view)
                        .asBitmap()
                        .load(Base64.decode(e.getAsJsonObject().get("image").getAsString(), Base64.DEFAULT))
                        .into(image);
            });
        }

        void setUP_cardViews(){
            constraintLayout.setOnTouchListener((view, motionEvent) -> {
                Global.activityToUserProfile();
                return false;
            });
        }

        void setUp_textView(){

            Global.current_user.getProfile().getAsJsonArray("data").forEach(e -> {
                Glide.with(view)
                        .asBitmap()
                        .load(Base64.decode(e.getAsJsonObject().get("image").getAsString(), Base64.DEFAULT))
                        .into(image);

                textView_name.setText(e.getAsJsonObject().get("name").getAsString());
                textView_id.setText(e.getAsJsonObject().get("id").getAsString());
            });
        }

        void setUp_button(){
            button.setOnClickListener(v -> {
                Http.logOut();
            });
        }

    }

}