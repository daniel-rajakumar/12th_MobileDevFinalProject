package com.example.a12th_mobiledevfinalproject.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.home.App;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Date;

public class Fragment_calendar extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
    }

    static class App {
        View view;
        TextView textView;
        private static final String TAG = "test";
        PageViewerAdapter_calendar pageViewerAdapter;
        ViewPager2 viewPager2;
        ChipNavigationBar menu;
        public static CalendarView calendarView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
            menu = view.findViewById(R.id.menu_calendar);
            viewPager2 = view.findViewById(R.id.viewPager_calendar);
            pageViewerAdapter = new PageViewerAdapter_calendar(com.example.a12th_mobiledevfinalproject.home.App.home);
            calendarView = view.findViewById(R.id.fragment_calendarView);
        }

        void setUp(){
            setUp_textView();
            setUp_pageViewer();
            setUp_Menu();
            setUp_Calendar();
        }

        private void setUp_Calendar(){
            calendarView.setOnDateChangeListener((calendarView, year, month, date) -> {
                Log.d(TAG, "setUp_Calendar: " + year);
                Fragment_work.App.updateList((month+1) + "/" + date);
                Fragment_event.App.updateList((month+1) + "/" + date + "/" + year);
                Fragment_schedule.App.updateList((month+1) + "/" + date);
            });
        }

        private void setUp_pageViewer(){
            pageViewerAdapter.addFragment(new Fragment_work());
            pageViewerAdapter.addFragment(new Fragment_event());
            pageViewerAdapter.addFragment(new Fragment_schedule());
            viewPager2.setAdapter(pageViewerAdapter);
            viewPager2.setUserInputEnabled(false);
            Log.d(TAG, "setUp_pageViewer: " + "flip");
            viewPager2.setCurrentItem(2);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            viewPager2.setCurrentItem(0);
                        }
                    }, 200
            );
        }

        private void setUp_Menu() {
            menu.setItemSelected(R.id.work, true);

            menu.setOnItemSelectedListener(i -> {
                int index = -1;

                switch (i) {
                    case R.id.work:     index = 0; break;
                    case R.id.event:    index = 1; break;
                    case R.id.schedule: index = 2; break;
                }

                Log.d(TAG, "setUp_Menu: " + "yesss");



                viewPager2.setCurrentItem(index, true);
            });
        }

        void setUp_textView(){
//            textView.setText(Global.prettifyJSON(Global.current_user.getGrade().toString()));
        }
    }

}

