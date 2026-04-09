package com.example.cafebookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ((TextView) findViewById(R.id.tvResultName))
                .setText(getIntent().getStringExtra("name"));
        ((TextView) findViewById(R.id.tvResultPhone))
                .setText(getIntent().getStringExtra("phone"));
        ((TextView) findViewById(R.id.tvResultService))
                .setText(getIntent().getStringExtra("service"));
        ((TextView) findViewById(R.id.tvResultQty))
                .setText(getIntent().getStringExtra("qty") + " item(s)");
        ((TextView) findViewById(R.id.tvResultAddress))
                .setText(getIntent().getStringExtra("address"));
        ((TextView) findViewById(R.id.tvResultPayment))
                .setText(getIntent().getStringExtra("payment"));

        findViewById(R.id.btnBackHome).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        findViewById(R.id.btnGoHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }
}