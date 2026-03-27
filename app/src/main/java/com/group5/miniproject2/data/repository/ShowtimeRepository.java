package com.group5.miniproject2.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.group5.miniproject2.data.dao.ShowtimeDao;
import com.group5.miniproject2.data.database.AppDatabase;
import com.group5.miniproject2.data.model.Showtime;
import com.group5.miniproject2.data.model.ShowtimeDetail;
import java.util.List;

public class ShowtimeRepository {
    private final ShowtimeDao showtimeDao;

    public ShowtimeRepository(Application app) {
        showtimeDao = AppDatabase.getInstance(app).showtimeDao();
    }

    public LiveData<List<ShowtimeDetail>> getByMovieAndDate(int movieId, String date) {
        return showtimeDao.getShowtimesByMovieAndDate(movieId, date);
    }

    public LiveData<List<ShowtimeDetail>> getByDate(String date) {
        return showtimeDao.getShowtimesByDate(date);
    }

    public LiveData<List<ShowtimeDetail>> getByTheaterAndDate(int theaterId, String date) {
        return showtimeDao.getShowtimesByTheaterAndDate(theaterId, date);
    }

    public void update(Showtime showtime) {
        AppDatabase.databaseWriteExecutor.execute(() -> showtimeDao.update(showtime));
    }

    public Showtime getShowtimeById(int id) throws Exception {
        return AppDatabase.databaseWriteExecutor.submit(
                () -> showtimeDao.getShowtimeById(id)).get();
    }
}