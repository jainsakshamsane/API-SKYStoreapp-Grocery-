package com.groceryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FlashscreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashscreen);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Check if the user is already logged in
                if (isLoggedIn()) {
                    // If logged in, go to WelcomePage
                    Intent intent = new Intent(FlashscreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // If not logged in, go to AdminLogin
                    Intent intent = new Intent(FlashscreenActivity.this, starting_1_activity.class);
                    startActivity(intent);
                }
                finish(); // Finish the current activity if needed
            }
        }, SPLASH_DISPLAY_TIME);
    }

    private boolean isLoggedIn() {
        // Check your login status logic using shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}

