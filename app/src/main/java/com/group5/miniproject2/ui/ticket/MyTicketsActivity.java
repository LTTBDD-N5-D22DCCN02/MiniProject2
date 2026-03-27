package com.group5.miniproject2.ui.ticket;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.group5.miniproject2.adapter.TicketAdapter;
import com.group5.miniproject2.databinding.ActivityMyTicketsBinding;
import com.group5.miniproject2.utils.SessionManager;
import com.group5.miniproject2.viewmodel.TicketViewModel;

public class MyTicketsActivity extends AppCompatActivity {
    private ActivityMyTicketsBinding binding;
    private TicketViewModel viewModel;
    private TicketAdapter adapter;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTicketsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new TicketAdapter(ticket -> showTicketDetail(ticket));
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTickets.setAdapter(adapter);

        int userId = sessionManager.getUserId();
        viewModel.getMyTickets(userId).observe(this, tickets -> {
            if (tickets == null || tickets.isEmpty()) {
                binding.rvTickets.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.rvTickets.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setTickets(tickets);
            }
        });
    }

    private void showTicketDetail(com.group5.movieticket.data.model.TicketDetail t) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("🎟 " + t.movieTitle)
                .setMessage(
                        "Mã đặt vé: " + t.bookingCode + "\n" +
                                "Ngày: " + t.showDate + " - " + t.showTime + "\n" +
                                "Rạp: " + t.theaterName + "\n" +
                                "Phòng: " + t.screenType + "\n" +
                                "Ghế: " + t.seatNumbers + "\n" +
                                "Tổng tiền: " + String.format("%,.0f đ", t.totalPrice) + "\n" +
                                "Trạng thái: " + t.status
                )
                .setPositiveButton("Đóng", null)
                .show();
    }
}