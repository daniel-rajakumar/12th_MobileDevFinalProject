package com.example.a12th_mobiledevfinalproject.home.fragments;

import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.example.a12th_mobiledevfinalproject.home.App;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_calendar_schedule;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_calendar_work;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarScheduleTable;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarWorkTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_schedule extends Fragment {

    private static final String TAG = "test";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
        Log.d(TAG, "onViewCreated: " + "schedule");
    }

    public static class App {
        static View view;
        public static TextView textView;
        private static final String TAG = "test";
        static List<CalendarScheduleTable> list;
        static RecyclerView recyclerView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
            list = new ArrayList<>();
            recyclerView = view.findViewById(R.id.fragment_list_schedule);
        }

        void setUp(){
            setUp_calendar();
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
////                    System.out.println("Hello World");
//                    com.example.a12th_mobiledevfinalproject.main_activity.App.mainActivity.runOnUiThread(() -> {
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
//                        Log.d(TAG, "run: " + "\n\n\n\n\n\n\n\n\n\n\n");
////                        updateList("hello");
//                    });
//                }
//            }, 0, 1000);
        }


        void setUp_calendar(){
            int month = new Date(Fragment_calendar.App.calendarView.getDate()).getMonth();
            int date = new Date(Fragment_calendar.App.calendarView.getDate()).getDate();
            Fragment_schedule.App.updateList((month+1) + "/" + date);
        }

        public static void updateList(String selectedDate) {
            list.clear();

            ((TextView)App.view.findViewById(R.id.fragment_textView_schedule)).setText("No School".toUpperCase());

            Log.d(TAG, "updateList: " + "schedule -> " + Global.current_user.getSchedule());
            Global.current_user.getSchedule().getAsJsonArray("data").forEach(e -> {
//                if (e.getAsJsonArray() == null) return;
                JsonElement head = e.getAsJsonObject().get("head");
                JsonElement body = e.getAsJsonObject().get("body");


                Log.d(TAG, "updateList: PASS! " + head + " => " + body);
                if (body.getAsJsonArray().size() == 0) {
                    Log.d(TAG, "updateList: FAIL! " + head + " => " + body);
                    return;
                }

                if (body.getAsJsonArray().get(0).toString().equals("\"no school\"")) {
                    return;
                }

                String date = head.getAsJsonArray().get(0).getAsString().substring(5, 10);
                String type = head.getAsJsonArray().get(1).getAsString();
                date = date.split("/")[0].replaceFirst("^0+", "") + "/"
                     + date.split("/")[1].replaceFirst("^0+", "");


//                Log.d(TAG, "updateList: date: " + date);
//                Log.d(TAG, "updateList: selectedDate: " + selectedDate);


                if (!date.equals(selectedDate)) return;

                Log.d(TAG, "updateList: " + "yes school: type: "  + type);
//                TextView textView = App.view.findViewById(R.id.fragment_textView_schedule);
//                textView.setText(type);
                ((TextView)App.view.findViewById(R.id.fragment_textView_schedule)).setText(type.toUpperCase());

                String finalDate = date;
                body.getAsJsonArray().forEach(k -> {
                    String period = k.getAsJsonArray().get(0).getAsString();
                    String time = k.getAsJsonArray().get(1).getAsString() + " - " + k.getAsJsonArray().get(2).getAsString();
                    String name = k.getAsJsonArray().get(3).getAsString();
                    String teacher = k.getAsJsonArray().get(4).getAsString();

                    String room;
                    if (!teacher.equals("Room")){
                        room = k.getAsJsonArray().get(5).getAsString();
                    } else {
                        room = "";
                        teacher = "";
                    }

//                    Log.d(TAG, "updateList: " + k.getAsJsonArray().toString());

                    CalendarScheduleTable calendarScheduleTable = new CalendarScheduleTable(finalDate, type, period, time, name, teacher, room);
                    list.add(calendarScheduleTable);
                });
//                Log.d(TAG, "updateList: " + body.getAsJsonArray().get(0).getAsJsonArray().get(0));
//                Log.d(TAG, "updateList: " + body.getAsJsonArray().get(0));
//                list.add(null);

            });


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.setAdapter(new Adapter_calendar_schedule(list, com.example.a12th_mobiledevfinalproject.home.App.home));

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

    }
}