package com.group5.miniproject2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.group5.miniproject2.data.model.User;
import com.group5.miniproject2.databinding.ActivityRegisterBinding;
import com.group5.miniproject2.utils.SessionManager;
import com.group5.miniproject2.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private UserViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String fullName = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email    = binding.etEmail.getText().toString().trim();
        String phone    = binding.etPhone.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirm  = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName))  { binding.tilFullName.setError("Vui lòng nhập họ tên"); return; }
        if (TextUtils.isEmpty(username))  { binding.tilUsername.setError("Vui lòng nhập tên đăng nhập"); return; }
        if (TextUtils.isEmpty(email))     { binding.tilEmail.setError("Vui lòng nhập email"); return; }
        if (TextUtils.isEmpty(password))  { binding.tilPassword.setError("Vui lòng nhập mật khẩu"); return; }
        if (password.length() < 6)        { binding.tilPassword.setError("Mật khẩu ít nhất 6 ký tự"); return; }
        if (!password.equals(confirm))    { binding.tilConfirmPassword.setError("Mật khẩu không khớp"); return; }

        binding.tilFullName.setError(null);
        binding.tilUsername.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        viewModel.register(new User(username, password, email, phone, fullName)).observe(this, result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRegister.setEnabled(true);
            if (result == -1L) {
                binding.tilUsername.setError("Tên đăng nhập đã tồn tại");
            } else if (result > 0) {
                sessionManager.saveLoginSession((int) result.longValue(), username, fullName);
                Toast.makeText(this, "Đăng ký thành công! Chào mừng " + fullName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại, thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}