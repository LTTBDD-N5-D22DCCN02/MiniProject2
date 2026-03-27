package com.group5.miniproject2.ui.booking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.miniproject2.data.model.Showtime;
import com.group5.miniproject2.databinding.ActivitySeatSelectionBinding;
import com.group5.miniproject2.utils.Constants;
import com.group5.miniproject2.viewmodel.ShowtimeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private ActivitySeatSelectionBinding binding;
    private ShowtimeViewModel viewModel;
    private int showtimeId;
    private Showtime currentShowtime;

    private final Set<String> selectedSeats = new LinkedHashSet<>();
    private Set<String> bookedSeats = new HashSet<>();
    private final Map<String, Button> seatButtons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showtimeId = getIntent().getIntExtra(Constants.EXTRA_SHOWTIME_ID, -1);
        viewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chon ghe ngoi");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnConfirm.setEnabled(false);
        binding.btnConfirm.setOnClickListener(v -> confirmBooking());

        loadShowtime();
    }

    private void loadShowtime() {
        new Thread(() -> {
            try {
                currentShowtime = viewModel.getShowtimeById(showtimeId);
                if (currentShowtime == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Khong tim thay suat chieu", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                    return;
                }
                // Parse ghế đã đặt
                String bs = currentShowtime.getBookedSeats();
                if (bs != null && !bs.trim().isEmpty()) {
                    bookedSeats = new HashSet<>(Arrays.asList(bs.split(",")));
                }
                runOnUiThread(this::buildSeatGrid);
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Loi tai du lieu", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void buildSeatGrid() {
        binding.gridSeats.removeAllViews();

        // +1 cột để chứa nhãn hàng A, B, C...
        binding.gridSeats.setColumnCount(Constants.SEAT_COLS + 1);

        for (int row = 0; row < Constants.SEAT_ROWS; row++) {
            // Nhãn hàng (A, B, C...)
            TextView rowLabel = new TextView(this);
            rowLabel.setText(String.valueOf(Constants.ROW_LABELS[row]));
            rowLabel.setGravity(Gravity.CENTER);
            rowLabel.setTextColor(Color.WHITE);
            rowLabel.setTextSize(13f);

            GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams();
            labelParams.width = dpToPx(28);
            labelParams.height = dpToPx(38);
            labelParams.setMargins(0, 4, 6, 4);
            rowLabel.setLayoutParams(labelParams);
            binding.gridSeats.addView(rowLabel);

            // Các ghế trong hàng
            for (int col = 1; col <= Constants.SEAT_COLS; col++) {
                // ✅ FIX: dùng String.valueOf(col) thay vì truyền int vào setText
                String seatId = String.valueOf(Constants.ROW_LABELS[row]) + col;

                Button seatBtn = new Button(this);
                seatBtn.setText(String.valueOf(col)); // ✅ ép kiểu rõ ràng
                seatBtn.setTextSize(10f);
                seatBtn.setPadding(0, 0, 0, 0);
                seatBtn.setAllCaps(false);

                GridLayout.LayoutParams seatParams = new GridLayout.LayoutParams();
                seatParams.width = dpToPx(36);
                seatParams.height = dpToPx(36);
                seatParams.setMargins(3, 3, 3, 3);
                seatBtn.setLayoutParams(seatParams);

                if (bookedSeats.contains(seatId)) {
                    // Ghế đã bán → xám, không bấm được
                    seatBtn.setBackgroundResource(com.group5.movieticket.R.drawable.bg_seat_booked);
                    seatBtn.setEnabled(false);
                    seatBtn.setTextColor(Color.LTGRAY);
                } else {
                    // Ghế trống → xanh, có thể chọn
                    seatBtn.setBackgroundResource(com.group5.movieticket.R.drawable.bg_seat_available);
                    seatBtn.setTextColor(Color.WHITE);
                    final String finalSeatId = seatId;
                    seatBtn.setOnClickListener(v -> toggleSeat(finalSeatId, seatBtn));
                }

                seatButtons.put(seatId, seatBtn);
                binding.gridSeats.addView(seatBtn);
            }
        }

        updateSummary();
    }

    private void toggleSeat(String seatId, Button seatBtn) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            seatBtn.setBackgroundResource(com.group5.movieticket.R.drawable.bg_seat_available);
            seatBtn.setTextColor(Color.WHITE);
        } else {
            if (selectedSeats.size() >= 8) {
                Toast.makeText(this, "Toi da 8 ghe moi lan dat", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedSeats.add(seatId);
            seatBtn.setBackgroundResource(com.group5.movieticket.R.drawable.bg_seat_selected);
            seatBtn.setTextColor(Color.WHITE);
        }
        updateSummary();
    }

    private void updateSummary() {
        int count = selectedSeats.size();
        float price = currentShowtime != null ? currentShowtime.getPrice() : 0f;
        float total = count * price;

        if (count == 0) {
            binding.tvSelectedSeats.setText("Chua chon ghe");
        } else {
            binding.tvSelectedSeats.setText("Ghe: " + String.join(", ", selectedSeats));
        }

        binding.tvTotalPrice.setText(String.format("Tong: %,.0f d", total));
        binding.btnConfirm.setEnabled(count > 0);
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) return;

        float price = currentShowtime != null ? currentShowtime.getPrice() : 0f;
        float total = selectedSeats.size() * price;

        Intent intent = new Intent(this, BookingConfirmActivity.class);
        intent.putExtra(Constants.EXTRA_SHOWTIME_ID, showtimeId);
        intent.putStringArrayListExtra("selected_seats", new ArrayList<>(selectedSeats));
        intent.putExtra("total_price", total);
        startActivity(intent);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}