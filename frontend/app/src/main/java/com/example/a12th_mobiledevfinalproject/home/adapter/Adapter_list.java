package com.example.a12th_mobiledevfinalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a12th_mobiledevfinalproject.R;
import com.example.a12th_mobiledevfinalproject.home.data_class.HomeTable;
import com.example.a12th_mobiledevfinalproject.home.data_class.ListTable;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter_list extends RecyclerView.Adapter<Adapter_list.MyViewHolder> {

    private static final String TAG = "test";
    List<ListTable> list;
    Context context;

    public Adapter_list(List<ListTable> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder._class.setText(list.get(position).getaClass());
        holder.date.setText(list.get(position).getDate());

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
        holder.mp.setText(list.get(position).getMp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, _class, date, score, score2, score3, score4, type, mp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_list_textView_name);
            _class = itemView.findViewById(R.id.list_list_textView_class);
            date = itemView.findViewById(R.id.list_list_textView_date);
            score = itemView.findViewById(R.id.list_list_textView_score);
            score2 = itemView.findViewById(R.id.list_list_textView_score2);
            score3 = itemView.findViewById(R.id.list_list_textView_score3);
            score4 = itemView.findViewById(R.id.list_list_textView_score4);
            type = itemView.findViewById(R.id.list_list_textView_type);
            mp = itemView.findViewById(R.id.list_list_textView_mp);
        }
    }
}
