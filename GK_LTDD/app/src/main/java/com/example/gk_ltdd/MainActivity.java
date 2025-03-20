package com.example.gk_ltdd;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = getIntent().getStringExtra("username");
        TextView welcomeText = findViewById(R.id.txtWelcome);
        welcomeText.setText("Chào mừng, " + username + "!");
    }
}
