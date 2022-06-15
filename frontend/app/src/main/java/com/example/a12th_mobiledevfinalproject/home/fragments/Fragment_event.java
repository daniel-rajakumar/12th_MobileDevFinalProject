package com.example.a12th_mobiledevfinalproject.home.fragments;

import android.app.blob.BlobHandle;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_calendar_event;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_calendar_work;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarEventTable;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarWorkTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Fragment_event extends Fragment {

    private static final String TAG = "test";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
        Log.d(TAG, "onViewCreated: " + "schedule");
    }

    static class App {
        View view;
        TextView textView;
        private static final String TAG = "test";
        static List<CalendarEventTable> list;
        static RecyclerView recyclerView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
            list = new ArrayList<>();
            recyclerView = view.findViewById(R.id.fragment_list_event);
        }


        void setUp(){
//            setUp_textView();
            setUp_calendar();
        }

        void setUp_calendar(){
            int year = new Date(Fragment_calendar.App.calendarView.getDate()).getYear() + 1900;
            int month = new Date(Fragment_calendar.App.calendarView.getDate()).getMonth();
            int date = new Date(Fragment_calendar.App.calendarView.getDate()).getDate();
            Fragment_event.App.updateList((month+1) + "/" + date + "/" + year);
        }


        public static void updateList(String selectedDate) {
            list.clear();

            Log.d(TAG, "updateList: " + Global.current_user.getEvents());
            Global.current_user.getEvents().getAsJsonArray("data").forEach(e -> {
                String name = e.getAsJsonArray().get(0).getAsString();
                String date = e.getAsJsonArray().get(1).getAsString();
                String year = date.split(" ")[2];
                String month = String.valueOf(monthStringToInt(date.split(" ")[1].split(",")[0]));
                String day = date.split(" ")[0]
                                 .replaceAll("[^0-9]","")
                                 .replaceFirst("^0*", "");


                CalendarEventTable calendarEventTable = new CalendarEventTable(name, date);


                if (selectedDate.equals(month + "/" + day + "/" + year)){
                    list.add(calendarEventTable);
                }

//                try {
////                    Log.d(TAG, "updateList: " + month + " => " + monthStringToInt(month));
//                    Log.d(TAG, "updateList: (" + month + "/" + day + "/" + year + ")");
//                } catch (Exception ignored){
//                    Log.d(TAG, "updateList: FAIL => " + date);
//                }
            });


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.setAdapter(new Adapter_calendar_event(list, com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private final Drawable divider = ContextCompat.getDrawable(com.example.a12th_mobiledevfinalproject.home.App.home, R.drawable.divider);

                @Override
                public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.onDrawOver(canvas, parent, state);

                    int dividerLeft = parent.getPaddingLeft();
                    int dividerRight = parent.getWidth() - parent.getPaddingRight();

                    int childCount = parent.getChildCount();
                    for (int i = 0; i <= childCount - 2; i++) {
                        View child = parent.getChildAt(i);

                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                        int dividerTop = child.getBottom() + params.bottomMargin;
                        assert divider != null;
                        int dividerBottom = dividerTop + divider.getIntrinsicHeight();

                        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                        divider.draw(canvas);
                    }

                }
            });

        }

        static int monthStringToInt(String month){
            switch (month){
                case "January": return 1;
                case "February": return 2;
                case "March": return 3;
                case "April": return 4;
                case "May": return 5;
                case "June": return 6;
                case "July": return 7;
                case "August": return 8;
                case "September": return 9;
                case "October": return 10;
                case "November": return 11;
                case "December": return 12;
                default: return -1;
            }
        }

    }

}