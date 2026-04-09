package com.example.cafebookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-login if already logged in
        if (getSharedPreferences("UserSession", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false)) {
            goHome();
            return;
        }

        setContentView(R.layout.activity_login);

        etUsername  = findViewById(R.id.etUsername);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String savedUser = prefs.getString("username", "");
        String savedPass = prefs.getString("password", "");

        if (username.equals(savedUser) && password.equals(savedPass)) {
            getSharedPreferences("UserSession", MODE_PRIVATE)
                    .edit().putBoolean("isLoggedIn", true).apply();
            goHome();
        } else {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void goHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}