package com.group5.movieticket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.chip.Chip;
import com.group5.movieticket.adapter.ShowtimeAdapter;
import com.group5.movieticket.databinding.FragmentShowtimesBinding;
import com.group5.movieticket.ui.booking.SeatSelectionActivity;
import com.group5.movieticket.utils.Constants;
import com.group5.movieticket.utils.SessionManager;
import com.group5.movieticket.viewmodel.ShowtimeViewModel;
import android.widget.Toast;

public class ShowtimeFragment extends Fragment {
    private FragmentShowtimesBinding binding;
    private ShowtimeViewModel viewModel;
    private ShowtimeAdapter adapter;
    private SessionManager sessionManager;
    private String selectedDate;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        binding = FragmentShowtimesBinding.inflate(inf, c, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);
        sessionManager = new SessionManager(requireContext());
        selectedDate = java.time.LocalDate.now().toString();

        adapter = new ShowtimeAdapter(showtime -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), com.group5.movieticket.ui.auth.LoginActivity.class));
                return;
            }
            Intent intent = new Intent(getContext(), SeatSelectionActivity.class);
            intent.putExtra(Constants.EXTRA_SHOWTIME_ID, showtime.id);
            startActivity(intent);
        });

        binding.rvShowtimes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvShowtimes.setAdapter(adapter);

        setupDateChips();
        loadShowtimes();
    }

    private void setupDateChips() {
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 0; i < 5; i++) {
            java.time.LocalDate d = today.plusDays(i);
            String date = d.toString();
            String label = i == 0 ? "Hôm nay" : i == 1 ? "Ngày mai" :
                    d.getDayOfMonth() + "/" + d.getMonthValue();
            Chip chip = new Chip(requireContext());
            chip.setText(label);
            chip.setCheckable(true);
            chip.setChecked(i == 0);
            chip.setOnCheckedChangeListener((btn, checked) -> {
                if (checked) { selectedDate = date; loadShowtimes(); }
            });
            binding.chipGroupDates.addView(chip);
        }
    }

    private void loadShowtimes() {
        viewModel.getByDate(selectedDate).observe(getViewLifecycleOwner(),
                list -> adapter.setShowtimes(list));
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}