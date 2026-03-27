package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "theaters")
public class Theater {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private float rating;
    @ColumnInfo(name = "total_seats")
    private int totalSeats;
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}