package com.example.ezcharity.fragments.adddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ezcharity.ConfirmationActivity;
import com.example.ezcharity.R;

public class qrCodePage extends AppCompatActivity {

    private ImageView qrCodeImage;
    private Button copyButton;
    private String qrCodeLink = "https://youtu.be/dQw4w9WgXcQ?si=iErVgl4w15IAG_-h"; // Replace with your QR code link

    private static final int DELAY_AFTER_BROWSER = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_page);

        // Find views by ID
        qrCodeImage = findViewById(R.id.qrCodeImg); // Replace with your ImageView ID
        copyButton = findViewById(R.id.btn_copy); // Replace with your Button ID

        // Set click listener for the QR code
        qrCodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(qrCodeLink);
                openBrowserAndNavigate(qrCodeLink);
            }
        });

        // Set click listener for the copy button
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(qrCodeLink);
                navigateToConfirmation(qrCodeLink);
            }
        });
    }

    // Method to open the browser and then navigate to ConfirmationActivity
    private void openBrowserAndNavigate(String link){
        openBrowser(link);
        new Handler().postDelayed(() -> navigateToConfirmation(link), DELAY_AFTER_BROWSER);
    }

    // Method to open the browser with the provided link
    private void openBrowser(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    // Method to copy the link to the clipboard
    private void copyToClipboard(String link) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code Link", link);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to copy link", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to navigate to ConfirmationActivity
    private void navigateToConfirmation(String link){
        String organization = getIntent().getStringExtra("organization");
        String amount = getIntent().getStringExtra("amount");
        Intent confirmationIntent = new Intent(this, ConfirmationActivity.class);
        confirmationIntent.putExtra("organization", organization);
        confirmationIntent.putExtra("amount", amount);
        confirmationIntent.putExtra("link", link);
        startActivity(confirmationIntent);
    }
}