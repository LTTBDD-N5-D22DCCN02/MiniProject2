package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;

public class TicketDetail {
    public int id;

    @ColumnInfo(name = "booking_code")
    public String bookingCode;

    @ColumnInfo(name = "seat_numbers")
    public String seatNumbers;

    @ColumnInfo(name = "total_price")
    public float totalPrice;

    @ColumnInfo(name = "booking_time")
    public long bookingTime;

    public String status;

    @ColumnInfo(name = "num_tickets")
    public int numTickets;

    // Alias từ JOIN - phải khớp chính xác với alias trong SQL
    public String movieTitle;

    public String moviePoster;

    public String theaterName;

    public String theaterCity;

    public String showDate;

    public String showTime;

    public String screenType;
}