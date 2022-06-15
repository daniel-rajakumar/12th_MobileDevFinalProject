package com.example.a12th_mobiledevfinalproject.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarWorkTable;

import java.util.List;

public class Adapter_calendar_work extends RecyclerView.Adapter<Adapter_calendar_work.MyViewHolder> {

    private static final String TAG = "test";
    List<CalendarWorkTable> list;
    Context context;

    public Adapter_calendar_work(List<CalendarWorkTable> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_work, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder._class.setText(list.get(position).getaClass());

        String[] scores = list.get(position).getScore().split("=>");
        switch (scores.length){
            case 4:
                holder.score.setText(scores[3]);
                holder.score2.setText(scores[2]);
                holder.score3.setText(scores[1]);
                holder.score4.setText(scores[0]);
                break;
            case 3:
                if (scores[0].startsWith("x")){
                    holder.score4.setText("");
                    holder.score3.setText(scores[0]);
                } else {
                    holder.score3.setText("");
                    holder.score4.setText(scores[0]);
                }
                holder.score.setText(scores[2]);
                holder.score2.setText(scores[1].replace("Assignment Pts: ", "_ / "));
                break;
            case 2:
                if (scores[0].equals("Not Graded")){
                    holder.score2.setText(scores[1].replace("Assignment Pts: ", "_ / "));
                    holder.score.setText(scores[0]);
                } else if (scores[0].startsWith("Assignment Pts: ")){
                    holder.score2.setText(scores[0].replace("Assignment Pts: ", "_ / "));
                    holder.score.setText(scores[1]);
                } else {
                    holder.score.setText(scores[1]);
                    holder.score2.setText(scores[0]);
                }
                holder.score3.setText("");
                holder.score4.setText("");
                break;
            case 1:
                holder.score.setText(scores[0]);
                holder.score2.setText("");
                holder.score3.setText("");
                holder.score4.setText("");
                break;
        }
        holder.type.setText(list.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, _class, score, score2, score3, score4, type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_schedule_textView_period);
            _class = itemView.findViewById(R.id.list_event_textView_name);
            score = itemView.findViewById(R.id.list_work_textView_score);
            score2 = itemView.findViewById(R.id.list_work_textView_score2);
            score3 = itemView.findViewById(R.id.list_work_textView_score3);
            score4 = itemView.findViewById(R.id.list_work_textView_score4);
            type = itemView.findViewById(R.id.list_work_textView_type);
        }
    }
}
