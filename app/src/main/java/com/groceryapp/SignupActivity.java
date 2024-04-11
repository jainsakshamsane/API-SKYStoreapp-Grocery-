package com.groceryapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.groceryapp.Models.SigninModel;
import com.groceryapp.Models.loginModel;
import com.groceryapp.Retrofitclient.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {

    EditText firstname, lastname, email, password, username, city, street, number, zipcode, phone;
    TextView signup, logintext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        city = findViewById(R.id.city);
        street = findViewById(R.id.street);
        number = findViewById(R.id.number);
        zipcode = findViewById(R.id.zipcode);
        phone = findViewById(R.id.phone);
        signup = findViewById(R.id.signup);
        logintext = findViewById(R.id.logintext);

        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(firstname) || isEmptyField(lastname) || isEmptyField(email) ||
                        isEmptyField(password) || isEmptyField(username) || isEmptyField(city) || isEmptyField(street) || isEmptyField(number) || isEmptyField(zipcode) || isEmptyField(phone)) {
                    showToast("Please enter Required details");
                    return;
                }

                if (phone.getText().toString().length() > 10) {
                    showToast("Phone number should not exceed 10 digits");
                    return;
                }

                SigninModel.NameModel nameModel = new SigninModel.NameModel();
                nameModel.setFirstname(firstname.getText().toString());
                nameModel.setLastname(lastname.getText().toString());

                SigninModel sign = new SigninModel();
                sign.setName(nameModel);

                // Set other fields in SigninModel
                sign.setEmail(email.getText().toString());
                sign.setPassword(password.getText().toString());
                sign.setUsername(username.getText().toString());
                sign.setPhone(phone.getText().toString());

                SigninModel.AddressModel addressModel = new SigninModel.AddressModel();
                addressModel.setCity(city.getText().toString());
                addressModel.setStreet(street.getText().toString());
                addressModel.setNumber(number.getText().toString());
                addressModel.setZipcode(zipcode.getText().toString());
                sign.setAddress(addressModel);

                SigninModel.GeoLocationModel geoLocationModel = new SigninModel.GeoLocationModel();
             //   geoLocationModel.setLat(latitude.getText().toString());
             //   geoLocationModel.setLon(longitude.getText().toString());
                addressModel.setGeolocation(geoLocationModel);

                Call<loginModel> call = RetrofitClient.getInstance().getMyApi().register(sign);
                call.enqueue(new Callback<loginModel>() {
                    @Override
                    public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                        if (response.isSuccessful()) {
                            //loginModel data = response.body();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
//                            Log.e("dataaa", response.body().toString());
//                            if (data != null && data.getName() != null) {
//                                String firstname = data.getName().getFirstname();
//                                String email = data.getEmail();
//
//                                if (firstname != null) {
//                                    Log.e("data user", firstname);
//                                }
//
//                                if (email != null) {
//                                    Log.e("data user", email);
//                                }


//                            } else {
//                                if (data == null) {
//                                    Log.e("signup response else", "Data is null");
//                                } else {
//                                    Log.e("signup response else", "NameModel is null");
//                                    if (data.getName() == null) {
//                                        Log.e("signup response else", "Car is null");
//                                    }
//                                }
//                            }
                        } else {
                            Log.e("signup response else", "Unsuccessful: " + response.code());
                            // Log the error body for further inspection
                            try {
                                Log.e("Error Body", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<loginModel> call, Throwable t) {
                        Log.e("API Error", "An error has occurred", t);
                        Toast.makeText(SignupActivity.this, "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean isEmptyField(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isGmailAccount(String email) {
        return email.toLowerCase().endsWith("@gmail.com");
    }
}
