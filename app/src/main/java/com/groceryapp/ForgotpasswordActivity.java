package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotpasswordActivity extends AppCompatActivity {

    EditText email;
    TextView submit, backtohome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);
        backtohome = findViewById(R.id.backtohome);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the text from the EditText
                String enteredEmail = email.getText().toString();

                // Check if the enteredEmail is not empty before displaying the Toast
                if (!enteredEmail.isEmpty()) {
                    // Display the Toast with the entered email
                    Toast.makeText(ForgotpasswordActivity.this, "Recovery link sent to mail id : " + enteredEmail, Toast.LENGTH_SHORT).show();

                    // Move to the LoginActivity
                    Intent intent = new Intent(ForgotpasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // If the email is empty, show an error Toast or handle it accordingly
                    Toast.makeText(ForgotpasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotpasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
