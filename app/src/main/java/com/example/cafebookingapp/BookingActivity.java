package com.example.cafebookingapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class BookingActivity extends AppCompatActivity {

    static final String CHANNEL_ID = "order_channel";

    Spinner spinnerService, spinnerPayment, spinnerSize;
    EditText etQuantity, etAddress, etNote;
    Button btnBook;
    TextView tvWelcome, tvValidation, tvTotal;
    CheckBox cbExtraShot, cbWhippedCream, cbCold;

    String userName, userPhone;
    DatabaseHelper DatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sessionPrefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences profilePrefs = getSharedPreferences("UserProfile", MODE_PRIVATE);

        boolean isLoggedIn = sessionPrefs.getBoolean("isLoggedIn", false);
        String savedUser = profilePrefs.getString("username", null);
        String savedPass = profilePrefs.getString("password", null);

        if (!isLoggedIn || savedUser == null || savedPass == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_booking);

        userName  = profilePrefs.getString("name", "User");
        userPhone = profilePrefs.getString("phone", "");

        DatabaseHelper = new DatabaseHelper(this);
        createNotificationChannel();

        tvWelcome      = findViewById(R.id.tvWelcome);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        spinnerSize    = findViewById(R.id.spinnerSize);

        etQuantity     = findViewById(R.id.etQuantity);
        etAddress      = findViewById(R.id.etAddress);
        etNote         = findViewById(R.id.etNote);

        cbExtraShot    = findViewById(R.id.cbExtraShot);
        cbWhippedCream = findViewById(R.id.cbWhippedCream);
        cbCold         = findViewById(R.id.cbCold);

        btnBook        = findViewById(R.id.btnBook);
        tvValidation   = findViewById(R.id.tvValidation);
        tvTotal        = findViewById(R.id.tvTotal);

        tvWelcome.setText("Hello, " + userName + "!");
        etAddress.setText(profilePrefs.getString("address", ""));

        setupListeners();
        btnBook.setOnClickListener(v -> showConfirmDialog());
        updatePrice();
    }

    private void setupListeners() {
        TextWatcher watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void afterTextChanged(Editable s) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                checkFields();
                updatePrice();
            }
        };

        etQuantity.addTextChangedListener(watcher);
        etAddress.addTextChangedListener(watcher);

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                checkFields();
                updatePrice();
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });

        spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                checkFields();
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                updatePrice();
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });

        cbExtraShot.setOnCheckedChangeListener((b, c) -> updatePrice());
        cbWhippedCream.setOnCheckedChangeListener((b, c) -> updatePrice());
        cbCold.setOnCheckedChangeListener((b, c) -> updatePrice());
    }

    private void checkFields() {
        boolean serviceOk  = spinnerService.getSelectedItemPosition() > 0;
        boolean paymentOk  = spinnerPayment.getSelectedItemPosition() > 0;
        boolean qtyOk      = !etQuantity.getText().toString().trim().isEmpty();
        boolean addressOk  = !etAddress.getText().toString().trim().isEmpty();

        boolean allFilled = serviceOk && paymentOk && qtyOk && addressOk;

        btnBook.setEnabled(allFilled);
        btnBook.setAlpha(allFilled ? 1.0f : 0.5f);
        tvValidation.setVisibility(allFilled ? View.GONE : View.VISIBLE);
    }

    private int calculatePrice() {
        int basePrice = 0;
        String coffee = spinnerService.getSelectedItem().toString();

        if (coffee.contains("Latte")) basePrice = 180;
        else if (coffee.contains("Cappuccino")) basePrice = 170;
        else if (coffee.contains("Americano")) basePrice = 140;
        else if (coffee.contains("Cold Coffee")) basePrice = 160;
        else if (coffee.contains("Espresso")) basePrice = 100;

        String size = spinnerSize.getSelectedItem().toString();
        if (size.contains("Medium")) basePrice += 20;
        else if (size.contains("Large")) basePrice += 40;

        if (cbExtraShot.isChecked()) basePrice += 30;
        if (cbWhippedCream.isChecked()) basePrice += 20;
        if (cbCold.isChecked()) basePrice += 10;

        int qty = 1;
        try {
            qty = Integer.parseInt(etQuantity.getText().toString());
        } catch (Exception e) {}

        return basePrice * qty;
    }

    private void updatePrice() {
        int total = calculatePrice();
        tvTotal.setText("Total: ₹" + total);
    }

    private void showConfirmDialog() {
        String service = spinnerService.getSelectedItem().toString();
        String qty     = etQuantity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String payment = spinnerPayment.getSelectedItem().toString();
        int totalPrice = calculatePrice();

        // Use custom layout for dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        
        ((TextView) dialogView.findViewById(R.id.dialogItem)).setText(service);
        ((TextView) dialogView.findViewById(R.id.dialogQty)).setText(qty);
        ((TextView) dialogView.findViewById(R.id.dialogTotal)).setText("₹" + totalPrice);
        ((TextView) dialogView.findViewById(R.id.dialogPayment)).setText(payment);
        ((TextView) dialogView.findViewById(R.id.dialogAddress)).setText("Deliver to: " + address);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            dialog.dismiss();
            placeOrder(service, qty, address, payment);
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void placeOrder(String service, String qty, String address, String payment) {
        String note = etNote.getText().toString().trim();
        boolean saved = DatabaseHelper.insertOrder(userName, userPhone, service, qty, address, payment, note);

        if (saved) {
            sendNotification(service);
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("name", userName);
            intent.putExtra("phone", userPhone);
            intent.putExtra("service", service);
            intent.putExtra("qty", qty);
            intent.putExtra("address", address);
            intent.putExtra("payment", payment);
            intent.putExtra("total", calculatePrice());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error placing order!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(String service) {
        Intent intent = new Intent(this, HistoryActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Order Placed! ☕")
                .setContentText(service + " is on its way.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(101, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Order Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);
        }
    }
}