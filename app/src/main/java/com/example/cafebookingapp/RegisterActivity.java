package com.example.cafebookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etPhone, etUsername, etPassword, etConfirm;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName     = findViewById(R.id.etName);
        etPhone    = findViewById(R.id.etPhone);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirm  = findViewById(R.id.etConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String name     = etName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm  = etConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() < 10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences
        getSharedPreferences("UserProfile", MODE_PRIVATE).edit()
                .putString("name",     name)
                .putString("phone",    phone)
                .putString("username", username)
                .putString("password", password)
                .apply();

        getSharedPreferences("UserSession", MODE_PRIVATE)
                .edit().putBoolean("isLoggedIn", true).apply();

        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}