package com.example.cafebookingapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    TextView tvEmpty;
    DatabaseHelper DatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView  = findViewById(R.id.listViewHistory);
        tvEmpty   = findViewById(R.id.tvEmpty);
        DatabaseHelper  = new DatabaseHelper(this);

        loadOrders();

        findViewById(R.id.btnClearAll).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear History")
                        .setMessage("Delete all order history?")
                        .setPositiveButton("Yes", (d, w) -> {
                            DatabaseHelper.clearAll();
                            loadOrders();
                            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show());
    }

    private void loadOrders() {
        ArrayList<String[]> orders = DatabaseHelper.getAllOrders();

        if (orders.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            // Build display strings
            ArrayList<String> display = new ArrayList<>();
            for (String[] row : orders) {
                display.add(
                        "☕ " + row[0] + "  x" + row[2] + "\n" +
                                "👤 " + row[1] + "\n" +
                                "📍 " + row[3] + "\n" +
                                "💳 " + row[4]
                );
            }

            listView.setAdapter(new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, display));
        }
    }
}