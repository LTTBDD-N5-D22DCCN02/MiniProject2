package com.group5.miniproject2.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String genre;
    private String director;
    private String cast;
    private int duration; // phút
    private float rating;
    @ColumnInfo(name = "poster_url")
    private String posterUrl;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private String language;
    @ColumnInfo(name = "age_rating")
    private String ageRating; // PG, T13, T16, T18
    @ColumnInfo(name = "is_now_showing")
    private boolean isNowShowing;
    @ColumnInfo(name = "is_coming_soon")
    private boolean isComingSoon;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }
    public boolean isNowShowing() { return isNowShowing; }
    public void setNowShowing(boolean nowShowing) { isNowShowing = nowShowing; }
    public boolean isComingSoon() { return isComingSoon; }
    public void setComingSoon(boolean comingSoon) { isComingSoon = comingSoon; }
}