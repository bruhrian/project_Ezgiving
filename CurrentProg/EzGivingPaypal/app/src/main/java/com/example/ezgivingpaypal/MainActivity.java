package com.example.ezgivingpaypal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    EditText edtAmount;
    Button btnPayment;

    String clientId = "AQTnlixTpNLyZkeisTQbCUnhiqEsGx7w7VIWDLJMfWR3OXnR49bEgrm3RrIN_gCem09OJF668nGFWMJH";

    int PAYPAL_REQUEST_CODE = 123;

    public static PayPalConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPayment = findViewById(R.id.btnPayment);

        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPayment();

            }
        });
    }

    private void getPayment() {

        String amounts = edtAmount.getText().toString();
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amounts)), "USD", "EzGiving", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PAYPAL_REQUEST_CODE){

            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(paymentConfirmation !=null) {


                try {
                    String paymentDetails = paymentConfirmation.toJSONObject().toString();
                    JSONObject object = new JSONObject(paymentDetails);


                } catch (JSONException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode== Activity.RESULT_CANCELED) {

                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();

                
            }

        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

            Toast.makeText(this, "invalid payment", Toast.LENGTH_SHORT).show();

        }

    }
}