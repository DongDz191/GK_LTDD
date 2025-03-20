package com.example.gk_ltdd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gk_ltdd.api.UserAPI;
import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtUsername, edtEmail, edtPassword;
    private ImageView imgAvatar;
    private Button btnRegister;
    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ UI
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnRegister = findViewById(R.id.btnRegister);

        userAPI = new UserAPI(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Kiểm tra ảnh đại diện
                String avatarBase64 = null;
                Drawable drawable = imgAvatar.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    Bitmap avatarBitmap = ((BitmapDrawable) drawable).getBitmap();
                    avatarBase64 = bitmapToBase64(avatarBitmap);
                }

                // Đăng ký tài khoản
                if (userAPI.register(name, username, password, email, avatarBase64)) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi đăng ký, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Chuyển đổi Bitmap thành chuỗi Base64
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
