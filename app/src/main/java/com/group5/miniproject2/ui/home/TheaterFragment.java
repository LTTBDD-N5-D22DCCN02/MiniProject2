package com.group5.movieticket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.group5.movieticket.adapter.TheaterAdapter;
import com.group5.movieticket.databinding.FragmentTheatersBinding;
import com.group5.movieticket.utils.Constants;
import com.group5.movieticket.viewmodel.TheaterViewModel;

public class TheaterFragment extends Fragment {

    private FragmentTheatersBinding binding;
    private TheaterViewModel theaterViewModel;
    private TheaterAdapter theaterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTheatersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        theaterViewModel = new ViewModelProvider(this).get(TheaterViewModel.class);

        // Bấm vào rạp → chuyển sang TheaterDetailActivity (trang mới)
        theaterAdapter = new TheaterAdapter(theater -> {
            Intent intent = new Intent(getContext(), TheaterDetailActivity.class);
            intent.putExtra(Constants.EXTRA_THEATER_ID, theater.getId());
            intent.putExtra("theater_name",    theater.getName());
            intent.putExtra("theater_address", theater.getAddress());
            intent.putExtra("theater_city",    theater.getCity());
            intent.putExtra("theater_phone",   theater.getPhone());
            intent.putExtra("theater_rating",  theater.getRating());
            startActivity(intent);
        });

        binding.rvTheaters.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTheaters.setAdapter(theaterAdapter);

        theaterViewModel.getAllTheaters().observe(getViewLifecycleOwner(),
                theaters -> theaterAdapter.setTheaters(theaters));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}