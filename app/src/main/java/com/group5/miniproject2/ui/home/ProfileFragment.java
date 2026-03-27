package com.group5.miniproject2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.group5.miniproject2.databinding.FragmentProfileBinding;
import com.group5.miniproject2.ui.auth.LoginActivity;
import com.group5.miniproject2.ui.ticket.MyTicketsActivity;
import com.group5.miniproject2.utils.SessionManager;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SessionManager sessionManager;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        binding = FragmentProfileBinding.inflate(inf, c, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        if (sessionManager.isLoggedIn()) {
            binding.tvUsername.setText(sessionManager.getFullName());
            binding.tvUserHandle.setText("@" + sessionManager.getUsername());
            binding.layoutLoggedIn.setVisibility(View.VISIBLE);
            binding.layoutGuest.setVisibility(View.GONE);
        } else {
            binding.layoutLoggedIn.setVisibility(View.GONE);
            binding.layoutGuest.setVisibility(View.VISIBLE);
        }

        binding.btnMyTickets.setOnClickListener(v ->
                startActivity(new Intent(getContext(), MyTicketsActivity.class)));

        binding.btnLogin.setOnClickListener(v ->
                startActivity(new Intent(getContext(), LoginActivity.class)));

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            binding.layoutLoggedIn.setVisibility(View.GONE);
            binding.layoutGuest.setVisibility(View.VISIBLE);
            android.widget.Toast.makeText(getContext(), "Đã đăng xuất", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}