package com.example.restaurantlist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] item = {"Sm", "Charitable", "Organ", "isation", "Enter"};
    String[] item1 = {"$1.00", "$5.00", "$10.00", "$25.00", "$50.00"};
    String[] item2 = {"Jan", "Feb", "Mar", "April", "May"};

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView2;
    AutoCompleteTextView autoCompleteTextView3;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems1;
    ArrayAdapter<String> adapterItems2;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First dropdown menu
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Org: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        // Second dropdown menu
        autoCompleteTextView2 = findViewById(R.id.auto_complete_txt2);
        adapterItems1 = new ArrayAdapter<>(this, R.layout.list_item, item1);
        autoCompleteTextView2.setAdapter(adapterItems1);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView1, View v, int i1, long lo) {
                String selectedAmount = adapterView1.getItemAtPosition(i1).toString();
                Toast.makeText(MainActivity.this, "Amount: " + selectedAmount, Toast.LENGTH_SHORT).show();
            }
        });

        // Third dropdown menu
        autoCompleteTextView3 = findViewById(R.id.auto_complete_txt3);
        adapterItems2 = new ArrayAdapter<>(this, R.layout.list_item, item2);
        autoCompleteTextView3.setAdapter(adapterItems2);
        autoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView1, View v1, int i2, long lon) {
                String selectedDate = adapterView1.getItemAtPosition(i2).toString();
                Toast.makeText(MainActivity.this, "Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        // Button need to change to be payment transfer info & proceed to payment pg
        button = findViewById(R.id.Proceed2Payment_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedOrg = autoCompleteTextView.getText().toString();
                String selectedAmount = autoCompleteTextView2.getText().toString();
                String selectedDate = autoCompleteTextView3.getText().toString();
                String total_items = "Org: " + selectedOrg  + ", Amount: " + selectedAmount + ", Date: " + selectedDate;
                Toast.makeText(getApplicationContext(), total_items, Toast.LENGTH_LONG).show();
            }
        });
    }
}
