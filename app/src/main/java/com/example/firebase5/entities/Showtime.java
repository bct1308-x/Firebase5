package com.example.firebase5.entities;

public class Showtime {
    public String movieId, time;

    public Showtime() {}

    public Showtime(String movieId, String time) {
        this.movieId = movieId;
        this.time = time;
    }
}