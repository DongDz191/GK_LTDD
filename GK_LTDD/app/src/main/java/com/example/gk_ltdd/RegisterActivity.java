package com.example.gk_ltdd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gk_ltdd.R;
import com.example.gk_ltdd.api.UserAPI;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import android.util.Base64;

public class RegisterActivity extends AppCompatActivity {

    // Các biến giao diện UI
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
                Bitmap avatarBitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
                String avatarBase64 = bitmapToBase64(avatarBitmap); // Chuyển đổi ảnh

                //if (userAPI.register( name, username, password, email, avatarBase64)) {
                    //Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                   // finish();
                //} else {
                    //Toast.makeText(RegisterActivity.this, "Lỗi đăng ký, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                //}
            }
        });
    }

    // Hàm chuyển đổi Bitmap thành chuỗi Base64
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream ByteArrayOutputStream = null;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream); // Sửa lại đúng cách
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
