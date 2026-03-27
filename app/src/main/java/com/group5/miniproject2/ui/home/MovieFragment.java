package com.group5.miniproject2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.group5.miniproject2.adapter.MovieAdapter;
import com.group5.miniproject2.databinding.FragmentMoviesBinding;
import com.group5.miniproject2.ui.movie.MovieDetailActivity;
import com.group5.miniproject2.utils.Constants;
import com.group5.miniproject2.viewmodel.MovieViewModel;

public class MovieFragment extends Fragment {

    private FragmentMoviesBinding binding;
    private MovieViewModel viewModel;
    private MovieAdapter nowShowingAdapter;
    private MovieAdapter comingSoonAdapter;
    private MovieAdapter filteredAdapter; // Adapter dùng khi đang filter/search

    // Danh sách thể loại
    private static final String[] GENRES = {
            "Action", "Drama", "Sci-Fi", "Comedy", "Horror", "Romance", "Crime", "Animation"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        setupAdapters();
        setupSearch();
        setupGenreChips();
        observeNormalData();
    }

    private void setupAdapters() {
        // Adapter cho danh sách "Đang chiếu" (horizontal)
        nowShowingAdapter = new MovieAdapter(movie -> openDetail(movie.getId()));
        binding.rvNowShowing.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvNowShowing.setAdapter(nowShowingAdapter);

        // Adapter cho danh sách "Sắp chiếu" (grid 2 cột)
        comingSoonAdapter = new MovieAdapter(movie -> openDetail(movie.getId()));
        binding.rvComingSoon.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvComingSoon.setAdapter(comingSoonAdapter);

        // Adapter cho kết quả filter/search (grid 2 cột)
        filteredAdapter = new MovieAdapter(movie -> openDetail(movie.getId()));
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Bỏ search → bỏ chip đang chọn → về trạng thái bình thường
                    binding.chipGroupGenre.clearCheck();
                    viewModel.clearFilter();
                    showNormalLayout();
                } else {
                    // Đang search → ẩn layout bình thường, hiện kết quả filter
                    binding.chipGroupGenre.clearCheck();
                    viewModel.search(query);
                    showFilterLayout("Kết quả tìm kiếm");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupGenreChips() {
        for (String genre : GENRES) {
            Chip chip = new Chip(requireContext());
            chip.setText(genre);
            chip.setCheckable(true);

            chip.setOnCheckedChangeListener((btn, isChecked) -> {
                if (isChecked) {
                    // Chọn chip → clear search → filter theo genre
                    binding.etSearch.setText("");
                    viewModel.filterByGenre(genre);
                    showFilterLayout("Thể loại: " + genre);
                } else {
                    // Bỏ chọn chip (click lại) → về bình thường
                    // Chỉ về bình thường nếu không có chip nào khác đang chọn
                    if (binding.chipGroupGenre.getCheckedChipId() == View.NO_ID) {
                        viewModel.clearFilter();
                        showNormalLayout();
                    }
                }
            });

            binding.chipGroupGenre.addView(chip);
        }
    }

    /**
     * Observe data cho màn hình bình thường (2 section: đang chiếu + sắp chiếu)
     */
    private void observeNormalData() {
        viewModel.getNowShowing().observe(getViewLifecycleOwner(),
                movies -> nowShowingAdapter.setMovies(movies));

        viewModel.getComingSoon().observe(getViewLifecycleOwner(),
                movies -> comingSoonAdapter.setMovies(movies));

        // ✅ Chỉ 1 observer duy nhất cho filteredMovies - không bị chồng
        viewModel.getFilteredMovies().observe(getViewLifecycleOwner(), movies -> {
            filteredAdapter.setMovies(movies);

            // Hiển thị thông báo nếu không có kết quả
            if (movies == null || movies.isEmpty()) {
                binding.tvFilterEmpty.setVisibility(View.VISIBLE);
                binding.rvFiltered.setVisibility(View.GONE);
            } else {
                binding.tvFilterEmpty.setVisibility(View.GONE);
                binding.rvFiltered.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Hiện layout filter (ẩn 2 section bình thường)
     */
    private void showFilterLayout(String title) {
        binding.layoutNormal.setVisibility(View.GONE);
        binding.layoutFiltered.setVisibility(View.VISIBLE);
        binding.tvFilterTitle.setText(title);

        // Gắn adapter vào rv filtered
        if (binding.rvFiltered.getAdapter() == null) {
            binding.rvFiltered.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.rvFiltered.setAdapter(filteredAdapter);
        }
    }

    /**
     * Hiện layout bình thường (ẩn layout filter)
     */
    private void showNormalLayout() {
        binding.layoutNormal.setVisibility(View.VISIBLE);
        binding.layoutFiltered.setVisibility(View.GONE);
    }

    private void openDetail(int movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}