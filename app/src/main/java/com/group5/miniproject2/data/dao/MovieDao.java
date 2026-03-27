package com.group5.miniproject2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.group5.miniproject2.data.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insertAll(Movie... movies);

    @Query("SELECT * FROM movies WHERE is_now_showing = 1 ORDER BY rating DESC")
    LiveData<List<Movie>> getNowShowing();

    @Query("SELECT * FROM movies WHERE is_coming_soon = 1 ORDER BY release_date ASC")
    LiveData<List<Movie>> getComingSoon();

    @Query("SELECT * FROM movies ORDER BY rating DESC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    LiveData<Movie> getMovieById(int id);

    // ✅ Tìm kiếm theo tên hoặc thể loại - không giới hạn now_showing
    @Query("SELECT * FROM movies WHERE " +
            "title LIKE '%' || :query || '%' OR " +
            "genre LIKE '%' || :query || '%' OR " +
            "director LIKE '%' || :query || '%' " +
            "ORDER BY rating DESC")
    LiveData<List<Movie>> searchMovies(String query);

    // ✅ Lọc theo thể loại - tìm trong TẤT CẢ phim, không chỉ now_showing
    @Query("SELECT * FROM movies WHERE genre LIKE '%' || :genre || '%' ORDER BY rating DESC")
    LiveData<List<Movie>> getByGenre(String genre);
}