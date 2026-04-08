package com.example.firebase5;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.firebase5.entities.Showtime;
import com.example.firebase5.entities.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    Button bookBtn;
    Spinner spinner;
    GridLayout grid;

    List<String> showtimes = new ArrayList<>();
    ArrayAdapter<String> adapter;

    List<String> selectedSeats = new ArrayList<>();
    String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        movieId = getIntent().getStringExtra("movieId");

        // 🔔 Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel",
                    "Movie Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        bookBtn = findViewById(R.id.bookBtn);
        spinner = findViewById(R.id.showtimeSpinner);
        grid = findViewById(R.id.seatGrid);

        // Spinner setup
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                showtimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Load showtimes (FILTERED)
        DatabaseReference ref = FirebaseDatabase.getInstance(
                "https://fir-5-dda23-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("showtimes");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                showtimes.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Showtime st = s.getValue(Showtime.class);

                    if (st != null && st.time != null && st.movieId != null
                            && st.movieId.equals(movieId)) {
                        showtimes.add(st.time);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        // Seats
        for (int i = 1; i <= 12; i++) {
            Button seat = new Button(this);

            seat.setText("A" + i);
            seat.setAllCaps(false);
            seat.setBackgroundColor(Color.LTGRAY);
            seat.setMinHeight(120);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);

            seat.setLayoutParams(params);

            seat.setOnClickListener(v -> {
                String s = seat.getText().toString();

                if (selectedSeats.contains(s)) {
                    selectedSeats.remove(s);
                    seat.setBackgroundColor(Color.LTGRAY);
                } else {
                    selectedSeats.add(s);
                    seat.setBackgroundColor(Color.GREEN);
                }
            });

            grid.addView(seat);
        }

        // Booking
        bookBtn.setOnClickListener(v -> {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Select seats", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spinner.getSelectedItem() == null) {
                Toast.makeText(this, "No showtime", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String time = spinner.getSelectedItem().toString();

            Ticket t = new Ticket(userId, movieId, time, selectedSeats);

            DatabaseReference ticketRef = FirebaseDatabase.getInstance(
                    "https://fir-5-dda23-default-rtdb.asia-southeast1.firebasedatabase.app"
            ).getReference("tickets");
            ticketRef.push().setValue(t);

            Toast.makeText(this, "Booked!", Toast.LENGTH_SHORT).show();

            // 🔔 Notification
            new Handler().postDelayed(() -> {
                showNotification("Your movie starts soon!");
            }, 10000);
        });
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void showNotification(String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "channel")
                        .setContentTitle("Movie Reminder")
                        .setContentText(text)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }
}