package com.example.cafebookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if onboarding was already shown
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("onboarding_shown", false)) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        TextView tvIcon = findViewById(R.id.tvIcon);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDesc = findViewById(R.id.tvDesc);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // Simple Fade-in and Slide-up animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1500);

        tvIcon.startAnimation(fadeIn);
        tvTitle.startAnimation(fadeIn);
        tvDesc.startAnimation(fadeIn);

        btnGetStarted.setOnClickListener(v -> {
            // Mark as shown
            prefs.edit().putBoolean("onboarding_shown", true).apply();
            goToLogin();
        });
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}