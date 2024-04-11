package com.groceryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.groceryapp.Models.SigninModel;
import com.groceryapp.Models.loginModel;
import com.groceryapp.Retrofitclient.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText username, loginpassword;
    TextView loginbutton, registertext, forgotpassword;

    private ApiService apiService;
    String userid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        loginpassword = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login);
        registertext = findViewById(R.id.registertext);
        forgotpassword = findViewById(R.id.forgotpassword);

        ImageView togglePassword = findViewById(R.id.togglePassword);
        EditText passwordEditText = findViewById(R.id.password);

        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                int inputType = (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ?
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD :
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

                passwordEditText.setInputType(inputType);
                // Move cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernames = username.getText().toString();
                String password = loginpassword.getText().toString();

                if (usernames.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Values", Toast.LENGTH_LONG).show();
                } else {
                    SigninModel signinData = new SigninModel();
                    signinData.setUsername(usernames);
                    signinData.setPassword(password);

                    Call<loginModel> call = apiService.login(signinData);
                    call.enqueue(new Callback<loginModel>() {
                        @Override
                        public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                loginModel signinResponse = response.body();

                                Log.e("datass", response.body().toString());
                                int userId = response.body().getId();

                                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                                SharedPreferences.Editor myedit = sharedPreferences.edit();
                                myedit.putInt("userID", userId);
                                myedit.putBoolean("isLoggedIn", true);
                                myedit.apply();

                                Log.e("userewrwrwrwr", String.valueOf(userId));

                                Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                showLoginFailedDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<loginModel> call, Throwable t) {
                            // Handle network errors or request failure
                            Toast.makeText(LoginActivity.this, "Login failed. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public Boolean validateUsername () {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("name cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword () {
        String val = loginpassword.getText().toString();
        if (val.isEmpty()) {
            loginpassword.setError("Password cannot be empty");
            return false;
        } else {
            loginpassword.setError(null);
            return true;
        }
    }

    private void showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Login Failed")
                .setMessage("Invalid credentials. Please check your email and password.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can optionally add functionality here if the user clicks "OK"
                    }
                })
                .show();
    }
}
