package com.example.gk_ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gk_ltdd.api.LoginAPI;
import com.example.gk_ltdd.api.UserAPI;
import com.example.gk_ltdd.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private UserAPI userAPI;
    private LoginAPI loginAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo UserAPI
        userAPI = new UserAPI(this);
        loginAPI = new LoginAPI(this);

        // Ánh xạ UI
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Bắt sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Bắt sự kiện mở giao diện đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            User user = loginAPI.login(username, password);

            if (user == null) {
                Toast.makeText(this, "Sai thông tin đăng nhập hoặc tài khoản chưa xác minh!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem user có dữ liệu không
            if (user.getUsername() == null) {
                Toast.makeText(this, "Lỗi: Không lấy được thông tin tài khoản!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", user.getUsername());
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Lỗi khi đăng nhập: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
