package com.example.cafebookingapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    View emptyState;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView   = findViewById(R.id.listViewHistory);
        emptyState = findViewById(R.id.tvEmpty);
        dbHelper   = new DatabaseHelper(this);

        loadOrders();

        findViewById(R.id.btnClearAll).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear History")
                        .setMessage("Are you sure you want to delete all order history?")
                        .setPositiveButton("Clear All", (d, w) -> {
                            dbHelper.clearAll();
                            loadOrders();
                            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show());
    }

    private void loadOrders() {
        ArrayList<String[]> orders = dbHelper.getAllOrders();

        if (orders.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() { return orders.size(); }
                @Override
                public Object getItem(int i) { return orders.get(i); }
                @Override
                public long getItemId(int i) { return i; }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = getLayoutInflater().inflate(R.layout.item_history, viewGroup, false);
                    }
                    String[] row = orders.get(i);
                    ((TextView)view.findViewById(R.id.tvItemName)).setText(row[0]);
                    ((TextView)view.findViewById(R.id.tvItemDetails)).setText("Qty: " + row[2] + " • " + row[4]);
                    ((TextView)view.findViewById(R.id.tvItemAddress)).setText("📍 " + row[3]);
                    return view;
                }
            });
        }
    }
}