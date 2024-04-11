package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.groceryapp.Models.SigninModel;
import com.groceryapp.Retrofitclient.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {
    EditText firstnames, lastnames, emails, usernames, passwords, citys, streets, numbers, zipcodes, latitudes, longitudes, phones;
    TextView save;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        back = findViewById(R.id.back);
        firstnames = findViewById(R.id.firstname);
        lastnames = findViewById(R.id.lastname);
        emails = findViewById(R.id.email);
        usernames = findViewById(R.id.username);
        passwords = findViewById(R.id.password);
        citys = findViewById(R.id.city);
        streets = findViewById(R.id.street);
        numbers = findViewById(R.id.number);
        zipcodes = findViewById(R.id.zipcode);
        latitudes = findViewById(R.id.latitude);
        longitudes = findViewById(R.id.longitude);
        phones = findViewById(R.id.phone);
        save = findViewById(R.id.save);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
                Toast.makeText(EditProfileActivity.this, "Save successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        String firstname = sharedpreferences.getString("firstname", "");
        String lastname = sharedpreferences.getString("lastname", "");
        String email = sharedpreferences.getString("email", "");
        String username = sharedpreferences.getString("username", "");
        String password = sharedpreferences.getString("password", "");
        String city = sharedpreferences.getString("city", "");
        String street = sharedpreferences.getString("street", "");
        String number = sharedpreferences.getString("number", "");
        String zipcode = sharedpreferences.getString("zipcode", "");
        String latitude = sharedpreferences.getString("latitude", "");
        String longitude = sharedpreferences.getString("longitude", "");
        String phone = sharedpreferences.getString("phone", "");

        firstnames.setText(firstname);
        lastnames.setText(lastname);
        emails.setText(email);
        usernames.setText(username);
        passwords.setText(password);
        citys.setText(city);
        streets.setText(street);
        numbers.setText(number);
        zipcodes.setText(zipcode);
        latitudes.setText(latitude);
        longitudes.setText(longitude);
        phones.setText(phone);
    }

    private void updateUser() {

        SharedPreferences sharedpreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt("id", -1);

        SigninModel userUpdateRequest = new SigninModel();
        userUpdateRequest.setUsername(usernames.getText().toString());
        userUpdateRequest.setPassword(passwords.getText().toString());
        userUpdateRequest.setPhone(phones.getText().toString());
        userUpdateRequest.setEmail(emails.getText().toString());
//        userUpdateRequest.getName().setFirstname(firstnames.getText().toString());
//        userUpdateRequest.getName().setLastname(lastnames.getText().toString());
//        userUpdateRequest.getAddress().setCity(citys.getText().toString());
//        userUpdateRequest.getAddress().setNumber(numbers.getText().toString());
//        userUpdateRequest.getAddress().setStreet(streets.getText().toString());
//        userUpdateRequest.getAddress().setZipcode(zipcodes.getText().toString());
    //    userUpdateRequest.getAddress().getGeolocation().setLon(longitudes.getText().toString());
    //    userUpdateRequest.getAddress().getGeolocation().setLat(latitudes.getText().toString());
        updateUser(userId, userUpdateRequest);
    }

    private void updateUser(int userId, SigninModel userUpdateRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService userService = retrofit.create(ApiService.class);

        Call<SigninModel> call = userService.updateUser(userId, userUpdateRequest);

        call.enqueue(new Callback<SigninModel>() {
            @Override
            public void onResponse(Call<SigninModel> call, Response<SigninModel> response) {
                if (response.isSuccessful()) {
                    SigninModel updatedUser = response.body();
                    // Handle the updated user as needed
                } else {
                    // Handle error response
                    Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SigninModel> call, Throwable t) {
                // Handle network error
                Toast.makeText(EditProfileActivity.this, "Network error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
