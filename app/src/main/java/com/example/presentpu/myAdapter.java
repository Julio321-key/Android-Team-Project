package com.example.presentpu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    Context context;
    ArrayList<attendanceModel> attendanceModelArrayList;

    public myAdapter(Context context, ArrayList<attendanceModel> attendanceModelArrayList) {
        this.context = context;
        this.attendanceModelArrayList = attendanceModelArrayList;
    }

    @NonNull
    @Override
    public myAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.myViewHolder holder, int position) {

        attendanceModel attendanceModel = attendanceModelArrayList.get(position);

        Picasso.get().load(attendanceModel.imageUrl).into(holder.imageView);
        holder.Name.setText(attendanceModel.Name);
        holder.Studentid.setText(attendanceModel.StudentId);
        holder.Datetime.setText(attendanceModel.DateTime);
        holder.Description.setText(attendanceModel.Description);

    }

    @Override
    public int getItemCount() {
        return attendanceModelArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView Name, Studentid, Datetime, Description;
        ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.namalkp);
            Studentid = itemView.findViewById(R.id.nim);
            Datetime = itemView.findViewById(R.id.waktu);
            Description = itemView.findViewById(R.id.keterangan);
            imageView = itemView.findViewById(R.id.IViews);
        }
    }
}
