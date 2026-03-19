package com.example.ezcharity.fragments.adddonation.PaypalFrag;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ezcharity.R;
import com.example.ezcharity.ConfirmationActivity; // Import ConfirmationActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;

public class PaypalBottomSheetDialog extends BottomSheetDialogFragment {

    private TextView orgTextView, amountTextView;
    private String organization, amount; // Store values for passing to ConfirmationActivity

    String clientId = "clientID";

    public static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("clientID");

    private final ActivityResultLauncher<Intent> paymentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        PaymentConfirmation paymentConfirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (paymentConfirmation != null) {
                            try {
                                String paymentDetails = paymentConfirmation.toJSONObject().toString();
                                JSONObject object = new JSONObject(paymentDetails);
                                Toast.makeText(getContext(), "Payment Success", Toast.LENGTH_SHORT).show();

                                // Navigate to ConfirmationActivity AFTER payment is confirmed
                                goToConfirmationActivity();

                                dismiss(); // Close the Bottom Sheet after navigation

                            } catch (JSONException e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Payment Failed or Canceled", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paypal_bottom_sheet_layout, container, false);

        // Initialize TextViews
        orgTextView = view.findViewById(R.id.OrgTVPaypal);
        amountTextView = view.findViewById(R.id.AmountTVPaypal);

        // Retrieve data from the bundle
        Bundle args = getArguments();
        if (args != null) {
            organization = args.getString("organization", "N/A");
            amount = args.getString("amount", "N/A");

            orgTextView.setText(organization);
            amountTextView.setText(amount);
        }

        // Handle button click
        Button actionButton = view.findViewById(R.id.actionButton);
        actionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Proceeding to PayPal!", Toast.LENGTH_SHORT).show();
            getPayment();
        });

        return view;
    }

    private void getPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", organization, PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        paymentLauncher.launch(intent);
    }

    private void goToConfirmationActivity() {
        Intent intent = new Intent(getActivity(), ConfirmationActivity.class);
        intent.putExtra("organization", organization);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }
}
