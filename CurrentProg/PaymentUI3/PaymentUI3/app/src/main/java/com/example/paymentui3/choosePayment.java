package com.example.paymentui3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class choosePayment extends AppCompatActivity {

    private Button qrCode, Paypal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        qrCode = findViewById(R.id.btn_qrcode);
        Paypal = findViewById(R.id.btn_paypal);
        qrCode.setOnClickListener(v -> {
            // Navigate to the next activity
            Intent intent = new Intent(choosePayment.this, qrCodePage.class);
            startActivity(intent);
        });
        Paypal.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Paypal dono", Toast.LENGTH_SHORT).show();
        });
    }

}