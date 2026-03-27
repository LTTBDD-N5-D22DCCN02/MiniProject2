package com.group5.miniproject2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.group5.miniproject2.databinding.ActivityLoginBinding;
import com.group5.miniproject2.ui.main.MainActivity;
import com.group5.miniproject2.utils.SessionManager;
import com.group5.miniproject2.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        binding.tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Liên hệ admin để đặt lại mật khẩu", Toast.LENGTH_SHORT).show());
    }

    private void attemptLogin() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        binding.tilUsername.setError(null);
        binding.tilPassword.setError(null);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        viewModel.login(username, password).observe(this, user -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);
            if (user != null) {
                sessionManager.saveLoginSession(user.getId(), user.getUsername(), user.getFullName());
                Toast.makeText(this, "Chào mừng " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                binding.etPassword.setText("");
            }
        });
    }
}