package com.group5.movieticket.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.group5.movieticket.data.dao.TheaterDao;
import com.group5.movieticket.data.database.AppDatabase;
import com.group5.movieticket.data.model.Theater;
import java.util.List;

public class TheaterRepository {
    private final TheaterDao theaterDao;

    public TheaterRepository(Application app) {
        theaterDao = AppDatabase.getInstance(app).theaterDao();
    }

    public LiveData<List<Theater>> getAllTheaters() { return theaterDao.getAllTheaters(); }
    public LiveData<List<Theater>> getByCity(String city) { return theaterDao.getByCity(city); }
}