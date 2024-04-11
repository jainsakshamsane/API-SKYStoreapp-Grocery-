package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    ImageView cartImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartImageView = findViewById(R.id.cartImageView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new All_products_Fragment()).commit();

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Set up cartImageView click listener
        cartImageView.setOnClickListener(v -> {
            // Start the CartActivity or any other activity you want to navigate to.
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    // Define the BottomNavigationView listener
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_item_1) {
                selectedFragment = new All_products_Fragment();
            } else if (item.getItemId() == R.id.nav_item_2) {
                selectedFragment = new Category_Fragment();
            } else if (item.getItemId() == R.id.nav_item_3) {
                selectedFragment = new Profile_Fragment();
            } else if (item.getItemId() == R.id.nav_item_4) {
                selectedFragment = new Order_Fragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }

            return false;
        }
    };
}
