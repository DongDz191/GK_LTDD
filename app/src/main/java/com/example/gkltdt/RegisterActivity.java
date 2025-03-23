package com.example.gkltdt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64; // Dùng để mã hoá/giải mã ảnh
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    // Layout container (for the registration form)
    private LinearLayout registrationFormLl;
    private EditText usernameEt, emailEt, passwordEt;
    private ImageView profileImageIv;
    private Button selectImageBtn, registerBtn, authBtn;

    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Khởi tạo Firebase Auth và DatabaseHelper
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ View

        usernameEt = findViewById(R.id.usernameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        profileImageIv = findViewById(R.id.profileImageIv);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        registerBtn = findViewById(R.id.btnRegister);
        authBtn = findViewById(R.id.btnAuth);

        // Chọn ảnh profile
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        // Xác thực email
        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Nhập đầy đủ các thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    authUser(username, email, password);
                }
            }
        });

        // Đăng ký
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password);
                }
            }
        });
    }

    // Mở Image Picker để chọn ảnh
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageIv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi tải hình ảnh lên", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Đăng ký user với Firebase (chỉ gửi email xác minh, chưa thêm vào DB)
    private void authUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký Firebase thành công => gửi email xác minh
                        FirebaseUser user = mAuth.getCurrentUser();
                        sendEmailVerification(user);

                        // Lưu tạm thông tin user vào SharedPreferences
                        byte[] imageBytes = getImageBytes();
                        storeUserData(username, email, password, imageBytes);

                        Toast.makeText(RegisterActivity.this, "Vui lòng xác minh email!", Toast.LENGTH_LONG).show();
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Đăng ký user
    private void registerUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //  kiểm tra email đã xác minh chưa
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // Nếu email đã xác minh => thêm vào DB (nếu chưa thêm)
                            insertUserIfNeeded();
                            Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();

                            // Có thể chuyển sang Activity khác
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Bạn cần xác nhận email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Đăng ký thất bại
                        String message;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            message = "Không tìm thấy tài khoản với email này";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            message = "Mật khẩu không hợp lệ";
                        } catch (Exception e) {
                            message = "Đăng ký thất bại: " + e.getMessage();
                        }
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Gửi email xác minh
    private void sendEmailVerification(FirebaseUser user) {
        if (user == null) return;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Link xác nhận email đã được gửi đến" + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi khi gửi link xác nhận", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Lấy mảng byte[] từ ImageView
    private byte[] getImageBytes() {
        if (profileImageIv.getDrawable() == null) {
            return null;
        }
        Bitmap bitmap = ((BitmapDrawable) profileImageIv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Lưu dữ liệu người dùng vào SharedPreferences (để sau này thêm vào DB khi đã verify)
    private void storeUserData(String username, String email, String password, byte[] imageBytes) {
        SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Chuyển byte[] thành base64 để lưu chuỗi
        String imageBase64 = null;
        if (imageBytes != null) {
            imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        editor.putString("USERNAME", username);
        editor.putString("EMAIL", email);
        editor.putString("PASSWORD", password);
        editor.putString("IMAGE_BASE64", imageBase64);
        editor.apply();
    }

    // Đọc dữ liệu từ SharedPreferences và chèn vào DB nếu chưa chèn
    private void insertUserIfNeeded() {
        SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        String spName = sp.getString("USERNAME", null);
        String spEmail = sp.getString("EMAIL", null);
        String spPassword = sp.getString("PASSWORD", null);
        String spImageBase64 = sp.getString("IMAGE_BASE64", null);

        // Nếu chưa có dữ liệu => có thể user đã được thêm trước đó
        if (spName == null || spEmail == null || spPassword == null) {
            return;
        }

        // Giải mã chuỗi base64 về mảng byte
        byte[] imageBytes = null;
        if (spImageBase64 != null) {
            imageBytes = Base64.decode(spImageBase64, Base64.DEFAULT);
        }

        // Chèn user vào DB
        long newRowId = dbHelper.insertUser(spName, spEmail, spPassword, imageBytes);
        if (newRowId != -1) {
            // Insert thành công => xoá dữ liệu để tránh chèn lại
            sp.edit().clear().apply();
        }
    }
}
