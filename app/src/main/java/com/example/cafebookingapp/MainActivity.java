package com.example.cafebookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvGreeting;
    Button btnOrder, btnHistory, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGreeting  = findViewById(R.id.tvGreeting);
        btnOrder    = findViewById(R.id.btnOrder);
        btnHistory  = findViewById(R.id.btnHistory);
        btnProfile  = findViewById(R.id.btnProfile);
        btnLogout   = findViewById(R.id.btnLogout);

        btnOrder.setOnClickListener(v ->
                startActivity(new Intent(this, BookingActivity.class)));

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        btnLogout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (d, w) -> {
                            getSharedPreferences("UserSession", MODE_PRIVATE)
                                    .edit().putBoolean("isLoggedIn", false).apply();
                            startActivity(new Intent(this, LoginActivity.class));
                            finishAffinity();
                        })
                        .setNegativeButton("No", null)
                        .show());
    }


    @Override
    protected void onResume() {
        super.onResume();
        String name = getSharedPreferences("UserProfile", MODE_PRIVATE)
                .getString("name", "there");
        tvGreeting.setText("Hey, " + name + "! ☕");
    }
}