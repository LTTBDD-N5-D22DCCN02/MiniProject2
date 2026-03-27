package com.group5.miniproject2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.group5.movieticket.data.model.Theater;

import java.util.List;

@Dao
public interface TheaterDao {
    @Insert
    void insertAll(Theater... theaters);

    @Query("SELECT * FROM theaters ORDER BY rating DESC")
    LiveData<List<Theater>> getAllTheaters();

    @Query("SELECT * FROM theaters WHERE city = :city ORDER BY rating DESC")
    LiveData<List<Theater>> getByCity(String city);

    @Query("SELECT * FROM theaters WHERE id = :id LIMIT 1")
    Theater getTheaterById(int id);
}