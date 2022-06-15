package com.example.a12th_mobiledevfinalproject.user_profile;

import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;

public class App {
    private static final String TAG = "test";
    UserProfile userProfile;
    TextView name, grade, id, counselor, locker, lunch;
    ImageView image;

    App(UserProfile userProfile){
        this.userProfile = userProfile;
        assign();
        setUps();
    }

    private void assign(){
        name = userProfile.findViewById(R.id.profile_textView_name);
        grade = userProfile.findViewById(R.id.profile_textView_grade);
        id = userProfile.findViewById(R.id.profile_textView_id);
        counselor = userProfile.findViewById(R.id.profile_textView_counselor);
        locker = userProfile.findViewById(R.id.profile_textView_locker);
        lunch = userProfile.findViewById(R.id.profile_textView_balance);
        image = userProfile.findViewById(R.id.profile_imageView_user);
    }

    private void setUps(){
        setUp_textViews();
    }

    private void setUp_textViews() {

        Global.current_user.getProfile().getAsJsonArray("data").forEach(e -> {
            name.setText(e.getAsJsonObject().get("name").getAsString());
            grade.setText(e.getAsJsonObject().get("grade").getAsString());
            id.setText(e.getAsJsonObject().get("id").getAsString());
            counselor.setText(e.getAsJsonObject().get("counselor").getAsString());
            locker.setText(e.getAsJsonObject().get("locker").getAsString());
            lunch.setText(e.getAsJsonObject().get("lunch").getAsString());


            Glide.with(userProfile)
                    .asBitmap()
                    .load(Base64.decode(e.getAsJsonObject().get("image").getAsString(), Base64.DEFAULT))
                    .into(image);
        });
    }
}
