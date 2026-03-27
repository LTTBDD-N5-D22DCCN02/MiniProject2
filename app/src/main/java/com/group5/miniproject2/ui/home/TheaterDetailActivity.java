package com.group5.movieticket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.group5.movieticket.adapter.ShowtimeAdapter;
import com.group5.movieticket.databinding.ActivityTheaterDetailBinding;
import com.group5.movieticket.ui.auth.LoginActivity;
import com.group5.movieticket.ui.booking.SeatSelectionActivity;
import com.group5.movieticket.utils.Constants;
import com.group5.movieticket.utils.SessionManager;
import com.group5.movieticket.viewmodel.ShowtimeViewModel;

import java.time.LocalDate;

public class TheaterDetailActivity extends AppCompatActivity {

    private ActivityTheaterDetailBinding binding;
    private ShowtimeViewModel showtimeViewModel;
    private ShowtimeAdapter showtimeAdapter;
    private SessionManager sessionManager;

    private int theaterId;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheaterDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy thông tin từ Intent
        theaterId = getIntent().getIntExtra(Constants.EXTRA_THEATER_ID, -1);
        String theaterName    = getIntent().getStringExtra("theater_name");
        String theaterAddress = getIntent().getStringExtra("theater_address");
        String theaterCity    = getIntent().getStringExtra("theater_city");
        String theaterPhone   = getIntent().getStringExtra("theater_phone");
        float  theaterRating  = getIntent().getFloatExtra("theater_rating", 0f);

        if (theaterId == -1) { finish(); return; }

        sessionManager    = new SessionManager(this);
        showtimeViewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);
        selectedDate      = LocalDate.now().toString();

        // Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(theaterName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Hiển thị thông tin rạp
        binding.tvTheaterName.setText(theaterName);
        binding.tvTheaterAddress.setText(theaterAddress + " - " + theaterCity);
        binding.tvTheaterPhone.setText("☎ " + theaterPhone);
        binding.tvTheaterRating.setText("⭐ " + theaterRating);

        setupShowtimeList();
        setupDateChips();
    }

    private void setupShowtimeList() {
        showtimeAdapter = new ShowtimeAdapter(showtime -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            Intent intent = new Intent(this, SeatSelectionActivity.class);
            intent.putExtra(Constants.EXTRA_SHOWTIME_ID, showtime.id);
            startActivity(intent);
        });

        binding.rvTheaterShowtimes.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(this));
        binding.rvTheaterShowtimes.setAdapter(showtimeAdapter);
    }

    private void setupDateChips() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            LocalDate date  = today.plusDays(i);
            String dateStr  = date.toString();
            String label;
            if (i == 0)      label = "Hôm nay";
            else if (i == 1) label = "Ngày mai";
            else             label = date.getDayOfMonth() + "/" + date.getMonthValue();

            Chip chip = new Chip(this);
            chip.setText(label);
            chip.setCheckable(true);
            chip.setChecked(i == 0);

            final String finalDate = dateStr;
            chip.setOnCheckedChangeListener((btn, checked) -> {
                if (checked) {
                    selectedDate = finalDate;
                    loadShowtimes();
                }
            });
            binding.chipGroupDates.addView(chip);
        }
        // Load hôm nay mặc định
        loadShowtimes();
    }

    private void loadShowtimes() {
        showtimeViewModel.getByTheaterAndDate(theaterId, selectedDate)
                .observe(this, showtimes -> {
                    if (showtimes == null || showtimes.isEmpty()) {
                        binding.tvNoShowtime.setVisibility(View.VISIBLE);
                        binding.rvTheaterShowtimes.setVisibility(View.GONE);
                    } else {
                        binding.tvNoShowtime.setVisibility(View.GONE);
                        binding.rvTheaterShowtimes.setVisibility(View.VISIBLE);
                        showtimeAdapter.setShowtimes(showtimes);
                    }
                });
    }
}