package com.group5.miniproject2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.group5.miniproject2.data.model.Showtime;
import com.group5.miniproject2.data.model.ShowtimeDetail;

import java.util.List;

@Dao
public interface ShowtimeDao {

    @Insert
    void insertAll(Showtime... showtimes);

    @Update
    void update(Showtime showtime);

    @Query("SELECT * FROM showtimes WHERE id = :id LIMIT 1")
    Showtime getShowtimeById(int id);

    @Query("SELECT s.id, s.movie_id, s.theater_id, s.show_date, s.show_time, " +
            "s.price, s.available_seats, s.total_seats, s.room_number, " +
            "s.screen_type, s.booked_seats, " +
            "m.title AS movieTitle, " +
            "m.genre AS movieGenre, " +
            "m.poster_url AS moviePoster, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.city AS theaterCity " +
            "FROM showtimes s " +
            "INNER JOIN movies m ON s.movie_id = m.id " +
            "INNER JOIN theaters t ON s.theater_id = t.id " +
            "WHERE s.movie_id = :movieId AND s.show_date = :date " +
            "ORDER BY s.show_time ASC")
    LiveData<List<ShowtimeDetail>> getShowtimesByMovieAndDate(int movieId, String date);

    @Query("SELECT s.id, s.movie_id, s.theater_id, s.show_date, s.show_time, " +
            "s.price, s.available_seats, s.total_seats, s.room_number, " +
            "s.screen_type, s.booked_seats, " +
            "m.title AS movieTitle, " +
            "m.genre AS movieGenre, " +
            "m.poster_url AS moviePoster, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.city AS theaterCity " +
            "FROM showtimes s " +
            "INNER JOIN movies m ON s.movie_id = m.id " +
            "INNER JOIN theaters t ON s.theater_id = t.id " +
            "WHERE s.show_date = :date " +
            "ORDER BY s.show_time ASC")
    LiveData<List<ShowtimeDetail>> getShowtimesByDate(String date);

    @Query("SELECT s.id, s.movie_id, s.theater_id, s.show_date, s.show_time, " +
            "s.price, s.available_seats, s.total_seats, s.room_number, " +
            "s.screen_type, s.booked_seats, " +
            "m.title AS movieTitle, " +
            "m.genre AS movieGenre, " +
            "m.poster_url AS moviePoster, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.city AS theaterCity " +
            "FROM showtimes s " +
            "INNER JOIN movies m ON s.movie_id = m.id " +
            "INNER JOIN theaters t ON s.theater_id = t.id " +
            "WHERE s.theater_id = :theaterId AND s.show_date = :date " +
            "ORDER BY m.title, s.show_time ASC")
    LiveData<List<ShowtimeDetail>> getShowtimesByTheaterAndDate(int theaterId, String date);
}