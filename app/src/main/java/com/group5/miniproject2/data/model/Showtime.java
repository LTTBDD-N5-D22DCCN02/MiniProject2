package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "showtimes",
        foreignKeys = {
                @ForeignKey(entity = Movie.class, parentColumns = "id",
                        childColumns = "movie_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Theater.class, parentColumns = "id",
                        childColumns = "theater_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("movie_id"), @Index("theater_id")}
)
public class Showtime {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "theater_id")
    private int theaterId;
    @ColumnInfo(name = "show_date")
    private String showDate;   // "2024-12-25"
    @ColumnInfo(name = "show_time")
    private String showTime;   // "14:30"
    private float price;
    @ColumnInfo(name = "available_seats")
    private int availableSeats;
    @ColumnInfo(name = "total_seats")
    private int totalSeats;
    @ColumnInfo(name = "room_number")
    private String roomNumber;
    @ColumnInfo(name = "screen_type")
    private String screenType; // "2D", "3D", "IMAX"
    @ColumnInfo(name = "booked_seats")
    private String bookedSeats; // "A1,A2,B3" comma-separated

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }
    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getScreenType() { return screenType; }
    public void setScreenType(String screenType) { this.screenType = screenType; }
    public String getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(String bookedSeats) { this.bookedSeats = bookedSeats; }
}