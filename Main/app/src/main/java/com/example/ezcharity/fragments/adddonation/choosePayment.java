package com.example.ezcharity.fragments.adddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.adddonation.PaypalFrag.PaypalBottomSheetDialog;

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
            String organization = getIntent().getStringExtra("organization");
            String amount = getIntent().getStringExtra("amount");
            Intent intent = new Intent(choosePayment.this, qrCodePage.class);
            intent.putExtra("organization", organization);
            intent.putExtra("amount", amount);
            startActivity(intent);
        });
        Paypal.setOnClickListener(v -> {
            String organization = getIntent().getStringExtra("organization");
            String amount = getIntent().getStringExtra("amount");

            // Create a bundle to send data
            Bundle bundle = new Bundle();
            bundle.putString("organization", organization);
            bundle.putString("amount", amount);

            // Pass the bundle to the BottomSheetDialog
            PaypalBottomSheetDialog paypalBottomSheetDialog = new PaypalBottomSheetDialog();
            paypalBottomSheetDialog.setArguments(bundle);
            paypalBottomSheetDialog.show(getSupportFragmentManager(), "BottomSheetTag");
        });
    }
}