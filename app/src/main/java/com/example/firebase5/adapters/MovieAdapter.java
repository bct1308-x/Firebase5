package com.example.firebase5.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase5.BookActivity;
import com.example.firebase5.R;
import com.example.firebase5.entities.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    List<Movie> list;
    List<String> keys;
    Context context;

    public MovieAdapter(List<Movie> list, List<String> keys, Context context) {
        this.list = list;
        this.keys = keys;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, duration;
        Button bookBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            bookBtn = itemView.findViewById(R.id.bookBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie m = list.get(position);

        holder.title.setText(m.title);
        holder.duration.setText(m.duration + " min");

        holder.bookBtn.setOnClickListener(v -> {
            Intent i = new Intent(context, BookActivity.class);
            i.putExtra("movieId", keys.get(position)); // ✅ FIXED
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}