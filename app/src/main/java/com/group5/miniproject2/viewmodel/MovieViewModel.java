package com.group5.miniproject2.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.group5.miniproject2.data.model.Movie;
import com.group5.miniproject2.data.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final MovieRepository repo;

    // LiveData gốc
    private final LiveData<List<Movie>> nowShowing;
    private final LiveData<List<Movie>> comingSoon;

    // MediatorLiveData để gộp filter + search vào 1 stream duy nhất
    // tránh việc observer bị chồng chất
    private final MediatorLiveData<List<Movie>> filteredMovies = new MediatorLiveData<>();

    // Track source hiện tại để remove đúng
    private LiveData<List<Movie>> currentSource = null;

    // Trạng thái filter hiện tại
    private final MutableLiveData<String> filterMode = new MutableLiveData<>("all");

    public MovieViewModel(Application app) {
        super(app);
        repo = new MovieRepository(app);
        nowShowing = repo.getNowShowing();
        comingSoon = repo.getComingSoon();
    }

    public LiveData<List<Movie>> getNowShowing() {
        return nowShowing;
    }

    public LiveData<List<Movie>> getComingSoon() {
        return comingSoon;
    }

    public LiveData<Movie> getMovieById(int id) {
        return repo.getMovieById(id);
    }

    /**
     * Trả về LiveData duy nhất cho filter/search
     * Dùng MediatorLiveData để tránh chồng observer
     */
    public LiveData<List<Movie>> getFilteredMovies() {
        return filteredMovies;
    }

    /**
     * Lọc theo thể loại - thay thế source cũ thay vì add thêm
     */
    public void filterByGenre(String genre) {
        switchSource(repo.getByGenre(genre));
        filterMode.setValue("genre:" + genre);
    }

    /**
     * Tìm kiếm theo từ khóa
     */
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            clearFilter();
            return;
        }
        switchSource(repo.searchMovies(query.trim()));
        filterMode.setValue("search:" + query);
    }

    /**
     * Xóa filter - quay về hiển thị tất cả
     */
    public void clearFilter() {
        switchSource(repo.getAllMovies());
        filterMode.setValue("all");
    }

    /**
     * ✅ Core fix: remove source cũ trước khi add source mới
     * Đây là nguyên nhân gây ra bug "chỉ ra đúng 2 phim"
     */
    private void switchSource(LiveData<List<Movie>> newSource) {
        if (currentSource != null) {
            filteredMovies.removeSource(currentSource);
        }
        currentSource = newSource;
        filteredMovies.addSource(currentSource, filteredMovies::setValue);
    }

    public String getCurrentFilterMode() {
        return filterMode.getValue() != null ? filterMode.getValue() : "all";
    }

    public boolean isFiltering() {
        String mode = filterMode.getValue();
        return mode != null && !mode.equals("all");
    }
}