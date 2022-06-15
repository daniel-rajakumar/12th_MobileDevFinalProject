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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.general.Global;
import com.example.a12th_mobiledevfinalproject.home.adapter.Adapter_home;
import com.example.a12th_mobiledevfinalproject.home.data_class.HomeTable;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_home extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new App(getView());
    }


    public static class App {
        View view;
        private static final String TAG = "test";
        List<HomeTable> list;
        String json;

        public static PowerSpinnerView spinner;
        RecyclerView recyclerView;

        App(View view){
            this.view = view;
            assign();
            setUp();
        }

        void assign(){
            list = new ArrayList<>();
            json = Global.prettifyJSON(Global.current_user.getGrade().toString());
            spinner = view.findViewById(R.id.fragment_home_spinner);
            recyclerView = view.findViewById(R.id.fragment_home_list);
        }

        void setUp(){
            setUp_spinner();
        }

        void setUp_spinner(){
            spinner.setItems(Arrays.asList("MP1", "MP2", "MP3", "MP4", "FP"));
            spinner.setLifecycleOwner(com.example.a12th_mobiledevfinalproject.main_activity.App.mainActivity);
            spinner.setOnSpinnerItemSelectedListener((i, s, i1, t1) -> updateList(i1));
            spinner.selectItemByIndex(3);
            updateList(3);
        }



        void updateList(int index){
            list.clear();
            Global.current_user.getGrade().getAsJsonArray("data").get(index).getAsJsonArray().forEach(e -> {
                String one = e.getAsJsonArray().get(0).getAsString();
                String teacher = e.getAsJsonArray().get(1).getAsString();
                String grade = e.getAsJsonArray().get(2).getAsString();
                String mail = e.getAsJsonArray().get(3).getAsString();

                HomeTable gradeTable = new HomeTable(one, teacher, grade, mail);
//                Log.d(TAG, "setUP_list: " + gradeTable);
                list.add(gradeTable);
            });



            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.a12th_mobiledevfinalproject.home.App.home));

            recyclerView.setAdapter(new Adapter_home(list, com.example.a12th_mobiledevfinalproject.home.App.home));

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
