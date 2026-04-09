package com.example.cafebookingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText etName, etPhone, etAddress;
    Button btnSave;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName    = findViewById(R.id.etName);
        etPhone   = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave   = findViewById(R.id.btnSave);
        tvStatus  = findViewById(R.id.tvStatus);

        loadProfile();
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadProfile() {
        SharedPreferences p = getSharedPreferences("UserProfile", MODE_PRIVATE);
        etName.setText(p.getString("name", ""));
        etPhone.setText(p.getString("phone", ""));
        etAddress.setText(p.getString("address", ""));
    }

    private void saveProfile() {
        String name    = etName.getText().toString().trim();
        String phone   = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        getSharedPreferences("UserProfile", MODE_PRIVATE).edit()
                .putString("name",    name)
                .putString("phone",   phone)
                .putString("address", address)
                .apply();

        tvStatus.setText("Profile saved!");
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    }
}