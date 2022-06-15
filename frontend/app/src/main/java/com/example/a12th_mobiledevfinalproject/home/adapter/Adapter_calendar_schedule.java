package com.example.a12th_mobiledevfinalproject.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarEventTable;
import com.example.a12th_mobiledevfinalproject.home.data_class.CalendarScheduleTable;

import java.util.List;

public class Adapter_calendar_schedule extends RecyclerView.Adapter<Adapter_calendar_schedule.MyViewHolder> {

    private static final String TAG = "test";
    List<CalendarScheduleTable> list;
    Context context;

    public Adapter_calendar_schedule(List<CalendarScheduleTable> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schedule, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.period.setText(list.get(position).getPeriod());
        holder.time.setText(list.get(position).getTime());
        holder.name.setText(list.get(position).getName());
        holder.teacher.setText(list.get(position).getTeacher());
        holder.room.setText(list.get(position).getRoom());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView period, time, name, teacher, room;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            period = itemView.findViewById(R.id.list_schedule_textView_period);
            time = itemView.findViewById(R.id.list_schedule_textView_time);
            name = itemView.findViewById(R.id.list_schedule_textView_name);
            teacher = itemView.findViewById(R.id.list_schedule_textView_teacher);
            room = itemView.findViewById(R.id.list_schedule_textView_room);
        }
    }
}
