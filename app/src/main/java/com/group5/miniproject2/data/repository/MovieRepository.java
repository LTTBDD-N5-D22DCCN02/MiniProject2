package com.group5.miniproject2.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.group5.miniproject2.data.dao.MovieDao;
import com.group5.miniproject2.data.database.AppDatabase;
import com.group5.miniproject2.data.model.Movie;
import java.util.List;

public class MovieRepository {
    private final MovieDao movieDao;

    public MovieRepository(Application app) {
        movieDao = AppDatabase.getInstance(app).movieDao();
    }

    public LiveData<List<Movie>> getNowShowing() { return movieDao.getNowShowing(); }
    public LiveData<List<Movie>> getComingSoon() { return movieDao.getComingSoon(); }
    public LiveData<List<Movie>> getAllMovies() { return movieDao.getAllMovies(); }
    public LiveData<List<Movie>> searchMovies(String q) { return movieDao.searchMovies(q); }
    public LiveData<Movie> getMovieById(int id) { return movieDao.getMovieById(id); }
    public LiveData<List<Movie>> getByGenre(String genre) { return movieDao.getByGenre(genre); }
}