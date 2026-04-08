package com.example.firebase5.entities;

public class Movie {
    public String title, image, duration;

    public Movie() {}

    public Movie(String title, String image, String duration) {
        this.title = title;
        this.image = image;
        this.duration = duration;
    }
}