package com.example.a12th_mobiledevfinalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class Adapter_home extends RecyclerView.Adapter<Adapter_home.MyViewHolder> {

    List<HomeTable> list;
    Context context;

    public Adapter_home(List<HomeTable> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView_name.setText(list.get(position).getName());
        holder.textView_teacher.setText(list.get(position).getTeacher());
        holder.textView_grade.setText(list.get(position).getGrade());

        holder.button.setOnClickListener(e -> {
            try {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ list.get(position).getMail().substring("mailto:".length()) });

                //need this to prompts email client only
                email.setType("message/rfc822");

                context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
            } catch (android.content.ActivityNotFoundException f) {
                Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name, textView_teacher, textView_grade;
        ImageButton button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.list_home_textView_name);
            textView_teacher = itemView.findViewById(R.id.list_home_textView_teacher);
            textView_grade = itemView.findViewById(R.id.list_home_textView_grade);
            button = itemView.findViewById(R.id.list_home_imageButton);
        }
    }
}
