package com.group5.movieticket.viewmodel;

import android.app.Application;
import androidx.lifecycle.*;
import com.group5.movieticket.data.model.Theater;
import com.group5.movieticket.data.repository.TheaterRepository;
import java.util.List;

public class TheaterViewModel extends AndroidViewModel {
    private final TheaterRepository repo;

    public TheaterViewModel(Application app) {
        super(app);
        repo = new TheaterRepository(app);
    }

    public LiveData<List<Theater>> getAllTheaters() { return repo.getAllTheaters(); }
    public LiveData<List<Theater>> getByCity(String city) { return repo.getByCity(city); }
}