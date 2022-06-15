package com.example.a12th_mobiledevfinalproject.home;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.fragments.Fragment_calendar;
import com.example.a12th_mobiledevfinalproject.home.fragments.Fragment_home;
import com.example.a12th_mobiledevfinalproject.home.fragments.Fragment_list;
import com.example.a12th_mobiledevfinalproject.home.fragments.Fragment_settings;
import com.example.a12th_mobiledevfinalproject.home.fragments.PageViewerAdapter_main;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Timer;
import java.util.TimerTask;

public class App {

    private static final String TAG = "test";
    public static Home home;
    ChipNavigationBar menu;
    ViewPager2 viewPager2;
    PageViewerAdapter_main pageViewerAdapter;

    App(Home home){
        App.home = home;
        assign();
        setUp();
    }

    void assign(){
        menu = home.findViewById(R.id.menu_main);
        viewPager2 = home.findViewById(R.id.viewPager_main);
        pageViewerAdapter = new PageViewerAdapter_main(home);
        pageViewerAdapter = new PageViewerAdapter_main(App.home);
    }

    void setUp(){
        setUp_pageViewer();
        bottomMenu();
        setUp_offlinePrompt();
    }

    // "offline" bar shows at top when user is offline.
    private void setUp_offlinePrompt(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                com.example.a12th_mobiledevfinalproject.main_activity.App.mainActivity.runOnUiThread(() -> {
                    if (Global.isUserOnline())
                        home.findViewById(R.id.home_textView_offline).setVisibility(View.GONE);
                    else
                        home.findViewById(R.id.home_textView_offline).setVisibility(View.VISIBLE);
                });
            }
        }, 0, 1000);
    }

    private void setUp_pageViewer(){
        pageViewerAdapter.addFragment(new Fragment_home());
        pageViewerAdapter.addFragment(new Fragment_list());
        pageViewerAdapter.addFragment(new Fragment_calendar());
        pageViewerAdapter.addFragment(new Fragment_settings());
        viewPager2.setAdapter(pageViewerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setCurrentItem(3);
        viewPager2.setCurrentItem(0);
    }

    private void bottomMenu() {
        menu.setItemSelected(R.id.home, true);

        menu.setOnItemSelectedListener(i -> {
            int index = -1;
            switch (i){
                case R.id.home: index = 0; break;
                case R.id.list: index = 1; break;
                case R.id.calendar: index = 2; break;
                case R.id.settings: index = 3; break;
            }

            Fragment_list.App.spinner.dismiss();
            Fragment_home.App.spinner.dismiss();
            viewPager2.setCurrentItem(index, true);
        });
    }

}
