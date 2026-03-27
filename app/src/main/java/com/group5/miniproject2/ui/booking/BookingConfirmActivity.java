package com.group5.miniproject2.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.group5.miniproject2.data.model.Showtime;
import com.group5.miniproject2.data.model.Ticket;
import com.group5.miniproject2.databinding.ActivityBookingConfirmBinding;
import com.group5.miniproject2.ui.ticket.MyTicketsActivity;
import com.group5.miniproject2.utils.Constants;
import com.group5.miniproject2.utils.SessionManager;
import com.group5.miniproject2.viewmodel.ShowtimeViewModel;
import com.group5.miniproject2.viewmodel.TicketViewModel;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingConfirmActivity extends AppCompatActivity {
    private ActivityBookingConfirmBinding binding;
    private TicketViewModel ticketViewModel;
    private ShowtimeViewModel showtimeViewModel;
    private SessionManager sessionManager;
    private int showtimeId;
    private ArrayList<String> selectedSeats;
    private float totalPrice;
    private Showtime showtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showtimeId    = getIntent().getIntExtra(Constants.EXTRA_SHOWTIME_ID, -1);
        selectedSeats = getIntent().getStringArrayListExtra("selected_seats");
        totalPrice    = getIntent().getFloatExtra("total_price", 0f);
        sessionManager= new SessionManager(this);
        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        showtimeViewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        loadShowtimeDetail();
        binding.btnBookNow.setOnClickListener(v -> processBooking());
    }

    private void loadShowtimeDetail() {
        new Thread(() -> {
            try {
                showtime = showtimeViewModel.getShowtimeById(showtimeId);
                runOnUiThread(() -> {
                    if (showtime == null) return;
                    binding.tvConfirmSeats.setText(String.join(", ", selectedSeats));
                    binding.tvConfirmSeatCount.setText(selectedSeats.size() + " ghế");
                    binding.tvConfirmDate.setText(showtime.getShowDate());
                    binding.tvConfirmTime.setText(showtime.getShowTime());
                    binding.tvConfirmRoom.setText("Phòng " + showtime.getRoomNumber() + " - " + showtime.getScreenType());
                    binding.tvConfirmPrice.setText(String.format("%,.0f đ", showtime.getPrice()) + "/ghế");
                    binding.tvConfirmTotal.setText(String.format("%,.0f đ", totalPrice));
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void processBooking() {
        binding.btnBookNow.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        String bookingCode = "MV" + System.currentTimeMillis() % 100000;
        String seatStr = String.join(",", selectedSeats);

        Ticket ticket = new Ticket();
        ticket.setUserId(sessionManager.getUserId());
        ticket.setShowtimeId(showtimeId);
        ticket.setSeatNumbers(seatStr);
        ticket.setTotalPrice(totalPrice);
        ticket.setBookingCode(bookingCode);
        ticket.setBookingTime(System.currentTimeMillis());
        ticket.setStatus(Constants.STATUS_CONFIRMED);
        ticket.setNumTickets(selectedSeats.size());

        ticketViewModel.bookTicket(ticket).observe(this, id -> {
            binding.progressBar.setVisibility(View.GONE);
            if (id != null && id > 0) {
                // Update booked seats in showtime
                new Thread(() -> {
                    try {
                        Showtime s = showtimeViewModel.getShowtimeById(showtimeId);
                        String existing = s.getBookedSeats() != null ? s.getBookedSeats() : "";
                        String updated  = existing.isEmpty() ? seatStr : existing + "," + seatStr;
                        s.setBookedSeats(updated);
                        s.setAvailableSeats(s.getAvailableSeats() - selectedSeats.size());
                        showtimeViewModel.update(s);
                    } catch (Exception ignored) {}
                }).start();

                showSuccessDialog(bookingCode);
            } else {
                binding.btnBookNow.setEnabled(true);
                Toast.makeText(this, "Đặt vé thất bại, thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog(String code) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("🎉 Đặt vé thành công!")
                .setMessage("Mã đặt vé: " + code + "\n\nCảm ơn bạn đã đặt vé tại CinemaApp!")
                .setPositiveButton("Xem vé của tôi", (d, w) -> {
                    // Mở MyTickets rồi xóa SeatSelection + BookingConfirm khỏi stack
                    Intent intent = new Intent(this, MyTicketsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Về trang chủ", (d, w) -> {
                    // Về MainActivity, xóa SeatSelection + BookingConfirm khỏi stack
                    Intent intent = new Intent(this,
                            com.group5.movieticket.ui.main.MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}