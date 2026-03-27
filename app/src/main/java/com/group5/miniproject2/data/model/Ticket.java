package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tickets",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id",
                        childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Showtime.class, parentColumns = "id",
                        childColumns = "showtime_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("user_id"), @Index("showtime_id")}
)
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "showtime_id")
    private int showtimeId;
    @ColumnInfo(name = "seat_numbers")
    private String seatNumbers; // "A1,A2"
    @ColumnInfo(name = "total_price")
    private float totalPrice;
    @ColumnInfo(name = "booking_code")
    private String bookingCode; // "MV-2024XXXX"
    @ColumnInfo(name = "booking_time")
    private long bookingTime;
    private String status; // "CONFIRMED", "CANCELLED"
    @ColumnInfo(name = "num_tickets")
    private int numTickets;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }
    public float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }
    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }
    public long getBookingTime() { return bookingTime; }
    public void setBookingTime(long bookingTime) { this.bookingTime = bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getNumTickets() { return numTickets; }
    public void setNumTickets(int numTickets) { this.numTickets = numTickets; }
}