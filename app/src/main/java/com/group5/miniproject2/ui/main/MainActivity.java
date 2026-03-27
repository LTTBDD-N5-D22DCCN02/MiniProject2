package com.group5.miniproject2.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.group5.miniproject2.R;
import com.group5.miniproject2.databinding.ActivityMainBinding;
import com.group5.miniproject2.ui.home.MovieFragment;
import com.group5.miniproject2.ui.home.ProfileFragment;
import com.group5.miniproject2.ui.home.ShowtimeFragment;
import com.group5.miniproject2.ui.home.TheaterFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load fragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new MovieFragment());
            binding.bottomNavigation.setSelectedItemId(R.id.nav_movies);
        }

        // Xử lý chọn tab bottom nav
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_movies) {
                fragment = new MovieFragment();
            } else if (id == R.id.nav_theaters) {
                fragment = new TheaterFragment();
            } else if (id == R.id.nav_showtimes) {
                fragment = new ShowtimeFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}