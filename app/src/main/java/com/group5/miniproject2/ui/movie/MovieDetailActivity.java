package com.group5.miniproject2.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.chip.Chip;

import com.group5.miniproject2.R;
import com.group5.miniproject2.adapter.ShowtimeAdapter;
import com.group5.miniproject2.databinding.ActivityMovieDetailBinding;
import com.group5.miniproject2.ui.auth.LoginActivity;
import com.group5.miniproject2.ui.booking.SeatSelectionActivity;
import com.group5.miniproject2.utils.Constants;
import com.group5.miniproject2.utils.SessionManager;
import com.group5.miniproject2.viewmodel.MovieViewModel;
import com.group5.miniproject2.viewmodel.ShowtimeViewModel;

import java.time.LocalDate;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding binding;
    private MovieViewModel movieViewModel;
    private ShowtimeViewModel showtimeViewModel;
    private SessionManager sessionManager;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy movieId từ Intent
        movieId = getIntent().getIntExtra(Constants.EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Không tìm thấy phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        sessionManager = new SessionManager(this);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        showtimeViewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        loadMovie();
        setupDateChips();
    }

    private void loadMovie() {
        movieViewModel.getMovieById(movieId).observe(this, movie -> {
            if (movie == null) return;

            binding.tvTitle.setText(movie.getTitle());
            binding.tvGenre.setText(movie.getGenre());
            binding.tvDirector.setText("Đạo diễn: " + movie.getDirector());
            binding.tvCast.setText("Diễn viên: " + movie.getCast());
            binding.tvDuration.setText(movie.getDuration() + " phút");
            binding.tvLanguage.setText(movie.getLanguage());
            binding.tvAgeRating.setText(movie.getAgeRating());
            binding.tvDescription.setText(movie.getDescription());
            binding.tvReleaseDate.setText("Khởi chiếu: " + movie.getReleaseDate());

            // RatingBar dùng thang 5 sao, rating gốc /10 → chia 2
            binding.ratingBar.setRating(movie.getRating() / 2f);
            binding.tvRating.setText(movie.getRating() + "/10");

            // Load poster
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(binding.ivPosterDetail);

            // Load backdrop (ảnh mờ phía sau)
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .centerCrop()
                    .into(binding.ivBackdrop);
        });
    }

    private void setupDateChips() {
        LocalDate today = LocalDate.now();

        // Tạo chip cho 5 ngày tiếp theo
        for (int i = 0; i < 5; i++) {
            LocalDate date = today.plusDays(i);
            String dateStr = date.toString(); // "2024-12-25"

            String label;
            if (i == 0) label = "Hôm nay";
            else if (i == 1) label = "Ngày mai";
            else label = date.getDayOfMonth() + "/" + date.getMonthValue();

            Chip chip = new Chip(this);
            chip.setText(label);
            chip.setCheckable(true);
            chip.setChecked(i == 0);

            final String finalDateStr = dateStr;
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    loadShowtimes(finalDateStr);
                }
            });

            binding.chipGroupDates.addView(chip);
        }

        // Load suất chiếu cho hôm nay mặc định
        loadShowtimes(today.toString());
    }

    private void loadShowtimes(String date) {
        showtimeViewModel.getByMovieAndDate(movieId, date).observe(this, showtimes -> {
            if (showtimes == null || showtimes.isEmpty()) {
                binding.tvNoShowtime.setVisibility(View.VISIBLE);
                binding.rvShowtimes.setVisibility(View.GONE);
            } else {
                binding.tvNoShowtime.setVisibility(View.GONE);
                binding.rvShowtimes.setVisibility(View.VISIBLE);

                ShowtimeAdapter adapter = new ShowtimeAdapter(showtime -> {
                    if (!sessionManager.isLoggedIn()) {
                        Toast.makeText(this,
                                "Vui lòng đăng nhập để đặt vé",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        return;
                    }
                    Intent intent = new Intent(this, SeatSelectionActivity.class);
                    intent.putExtra(Constants.EXTRA_SHOWTIME_ID, showtime.id);
                    startActivity(intent);
                });

                binding.rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
                binding.rvShowtimes.setAdapter(adapter);
                adapter.setShowtimes(showtimes);
            }
        });
    }
}