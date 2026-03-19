package com.example.ezcharity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezcharity.fragments.addbooking.addBooking;
import com.example.ezcharity.fragments.adddonation.addDonation;
import com.example.ezcharity.fragments.forum.forum;
import com.example.ezcharity.fragments.home.HomeFragment;
import com.example.ezcharity.fragments.logs.logs;
import com.example.ezcharity.fragments.mybookings.myBookings;
import com.example.ezcharity.fragments.topnav.AddListing;
import com.example.ezcharity.loginstuff.Login;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        //textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            //textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if(item.getItemId() == R.id.nav_add_booking){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addBooking()).commit();
        } else if(item.getItemId() == R.id.nav_add_donation){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addDonation()).commit();
        } else if(item.getItemId() == R.id.nav_my_bookings){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new myBookings()).commit();
        } else if(item.getItemId() == R.id.nav_forum){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new forum()).commit();
        } else if(item.getItemId() == R.id.nav_logs){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new logs()).commit();
        } else if(item.getItemId() == R.id.add_listing){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddListing()).commit();
        } else {
            Toast.makeText(this, "You Have Logged Out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}