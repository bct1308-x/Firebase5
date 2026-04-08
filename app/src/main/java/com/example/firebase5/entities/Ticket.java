package com.example.firebase5.entities;

import java.util.List;

public class Ticket {
    public String userId, movieId, time;
    public List<String> seats;

    public Ticket() {}

    public Ticket(String userId, String movieId, String time, List<String> seats) {
        this.userId = userId;
        this.movieId = movieId;
        this.time = time;
        this.seats = seats;
    }
}
