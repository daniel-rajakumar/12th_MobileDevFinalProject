package com.example.a12th_mobiledevfinalproject.home.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_home;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_list;
import com.example.a12th_mobiledevfinalproject.home.data_class.HomeTable;
import com.example.a12th_mobiledevfinalproject.home.data_class.ListTable;
import com.example.a12th_mobiledevfinalproject.main_activity.App;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Fragment_list extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
    }


    public static class App {
        View view;
        public static TextView textView;
        public static PowerSpinnerView spinner;
        private static final String TAG = "test";

        List<ListTable> list;
        RecyclerView recyclerView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
//            textView = view.findViewById(R.id.fragment_textView_list);
            list = new ArrayList<>();
            spinner = view.findViewById(R.id.fragment_powerSpinnerView);
            recyclerView = view.findViewById(R.id.fragment_list_list);
        }

        void setUp(){
            setUp_spinner();
        }


        void setUp_spinner(){
            List<String> courseList = new LinkedList<>();
            courseList.add("All");

            Global.current_user.getGrade().getAsJsonArray("data")
                                .get(4).getAsJsonArray().forEach(e -> {
//                Log.d(TAG, "setUp_spinner: " + e.getAsJsonArray().get(0).getAsString());

                String courseName = e.getAsJsonArray()
                                     .get(0).getAsString();
                courseList.add(courseName);
            });

            spinner.setItems(courseList);
            spinner.selectItemByIndex(0);
            updateList("All");
            spinner.setLifecycleOwner(com.example.a12th_mobiledevfinalproject.main_activity.App.mainActivity);
            spinner.setOnSpinnerItemSelectedListener((i, o, i1, t1) -> {
//                Log.d(TAG, "setUp_spinner: " + i);
//                Log.d(TAG, "setUp_spinner: " + o);
//                Log.d(TAG, "setUp_spinner: " + i1);
//                Log.d(TAG, "setUp_spinner: " + t1);
                updateList(t1.toString());
            });
        }


        void updateList(String selectedCourse) {
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
                ListTable listTables = new ListTable(mp, _class, teacher, date, type, name, score, comment);

                if (selectedCourse.equals("All") || _class.equals(selectedCourse))
                    list.add(listTables);

//                Log.d(TAG, "updateList: " + listTables);
            });


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.setAdapter(new Adapter_list(list, com.example.a12th_mobiledevfinalproject.home.App.home));

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

