package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;

public class ShowtimeDetail {
    public int id;

    @ColumnInfo(name = "movie_id")
    public int movieId;

    @ColumnInfo(name = "theater_id")
    public int theaterId;

    @ColumnInfo(name = "show_date")
    public String showDate;

    @ColumnInfo(name = "show_time")
    public String showTime;

    public float price;

    @ColumnInfo(name = "available_seats")
    public int availableSeats;

    @ColumnInfo(name = "total_seats")
    public int totalSeats;

    @ColumnInfo(name = "room_number")
    public String roomNumber;

    @ColumnInfo(name = "screen_type")
    public String screenType;

    @ColumnInfo(name = "booked_seats")
    public String bookedSeats;

    // JOIN fields - tên phải khớp CHÍNH XÁC với alias trong SQL query
    public String movieTitle;

    public String movieGenre;

    // Query trả về "moviePoster" → field phải tên là moviePoster (không dùng @ColumnInfo)
    public String moviePoster;

    public String theaterName;

    public String theaterAddress;

    public String theaterCity;
}