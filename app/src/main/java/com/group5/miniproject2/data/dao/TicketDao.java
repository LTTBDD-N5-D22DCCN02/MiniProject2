package com.group5.miniproject2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.group5.movieticket.data.model.Ticket;
import com.group5.movieticket.data.model.TicketDetail;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    long insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Query("SELECT t.id, t.booking_code, t.seat_numbers, t.total_price, " +
            "t.booking_time, t.status, t.num_tickets, " +
            "m.title AS movieTitle, " +
            "m.poster_url AS moviePoster, " +
            "th.name AS theaterName, " +
            "th.city AS theaterCity, " +
            "s.show_date AS showDate, " +
            "s.show_time AS showTime, " +
            "s.screen_type AS screenType " +
            "FROM tickets t " +
            "INNER JOIN showtimes s ON t.showtime_id = s.id " +
            "INNER JOIN movies m ON s.movie_id = m.id " +
            "INNER JOIN theaters th ON s.theater_id = th.id " +
            "WHERE t.user_id = :userId " +
            "ORDER BY t.booking_time DESC")
    LiveData<List<TicketDetail>> getTicketsByUser(int userId);
}