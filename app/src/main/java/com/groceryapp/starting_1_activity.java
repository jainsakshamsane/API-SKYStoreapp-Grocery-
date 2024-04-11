package com.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class starting_1_activity extends AppCompatActivity {
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_1_activity);

        // Find the "skip" TextView by its id
        skip = findViewById(R.id.skip);

        // Set an OnClickListener on the "skip" TextView
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the next activity
                startActivity(new Intent(starting_1_activity.this, LoginActivity.class));

                // Finish the current activity if you don't want to go back to it
                finish();
            }
        });
    }
}
