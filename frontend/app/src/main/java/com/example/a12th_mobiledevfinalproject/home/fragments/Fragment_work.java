package com.example.a12th_mobiledevfinalproject.home.fragments;

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

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_calendar_work;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarWorkTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_work extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
    }

    static class App {
        View view;
        private static final String TAG = "test";
        static List<CalendarWorkTable> list;
        static RecyclerView recyclerView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }


        void assign(){
            list = new ArrayList<>();
            recyclerView = view.findViewById(R.id.fragment_list_work);
        }

        void setUp(){

            setUp_calendar();
        }

        void setUp_calendar(){
            int month = new Date(Fragment_calendar.App.calendarView.getDate()).getMonth();
            int date = new Date(Fragment_calendar.App.calendarView.getDate()).getDate();
            Fragment_work.App.updateList((month+1) + "/" + date);
        }

        public static void updateList(String selectedDate) {
            list.clear();

            Global.current_user.getList().getAsJsonArray("data").forEach(e -> {
//                Log.d(TAG, "updateList: " + e);
                String mp = e.getAsJsonArray().get(0).getAsString();
                String _class = e.getAsJsonArray().get(1).getAsString();
                String teacher = e.getAsJsonArray().get(2).getAsString();
                String date = e.getAsJsonArray().get(3).getAsString();
                String type = e.getAsJsonArray().get(4).getAsString();
                String name = e.getAsJsonArray().get(5).getAsString();
                String score = e.getAsJsonArray().get(6).getAsString().replace("\n", "=>");
                String comment = e.getAsJsonArray().get(7).getAsString();
                CalendarWorkTable listTables = new CalendarWorkTable(mp, _class, teacher, date, type, name, score, comment);

                try {
                    if (date.split(" ")[1].equals(selectedDate))
                        list.add(listTables);
                } catch (Exception _e){
                    Log.d(TAG, "updateList: ERRORRRRRRRRRRR => " + date);
                }

            });


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.setAdapter(new Adapter_calendar_work(list, com.example.a12th_mobiledevfinalproject.home.App.home));


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