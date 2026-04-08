package com.example.firebase5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase5.adapters.MovieAdapter;
import com.example.firebase5.entities.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Movie> list = new ArrayList<>();
    List<String> keys = new ArrayList<>();

    MovieAdapter adapter;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance(
                "https://fir-5-dda23-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("movies");

        ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                keys.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Movie m = s.getValue(Movie.class);

                    if (m != null) {
                        list.add(m);
                        keys.add(s.getKey());
                    }
                }

                adapter = new MovieAdapter(list, keys, MainActivity.this);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}