package com.group5.movieticket.viewmodel;

import android.app.Application;
import androidx.lifecycle.*;
import com.group5.movieticket.data.model.Showtime;
import com.group5.movieticket.data.model.ShowtimeDetail;
import com.group5.movieticket.data.repository.ShowtimeRepository;
import java.util.List;

public class ShowtimeViewModel extends AndroidViewModel {
    private final ShowtimeRepository repo;

    public ShowtimeViewModel(Application app) {
        super(app);
        repo = new ShowtimeRepository(app);
    }

    public LiveData<List<ShowtimeDetail>> getByMovieAndDate(int movieId, String date) {
        return repo.getByMovieAndDate(movieId, date);
    }

    public LiveData<List<ShowtimeDetail>> getByDate(String date) {
        return repo.getByDate(date);
    }

    public LiveData<List<ShowtimeDetail>> getByTheaterAndDate(int theaterId, String date) {
        return repo.getByTheaterAndDate(theaterId, date);
    }

    public Showtime getShowtimeById(int id) throws Exception {
        return repo.getShowtimeById(id);
    }

    public void update(Showtime showtime) { repo.update(showtime); }
}