package com.groceryapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.groceryapp.Models.loginModel;
import com.groceryapp.Retrofitclient.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile_Fragment extends Fragment {

    TextView firstname, lastname, email, username, password, city, street, number, zipcode, latitude, longitude, phone;
    ImageView editbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        editbutton = view.findViewById(R.id.editbutton);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView logoutButton = view.findViewById(R.id.logout1);
        logoutButton.setOnClickListener(v -> logoutUser());

        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        email = view.findViewById(R.id.email);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        city = view.findViewById(R.id.city);
        street = view.findViewById(R.id.street);
        number = view.findViewById(R.id.number);
        zipcode = view.findViewById(R.id.zipcode);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
        phone = view.findViewById(R.id.phone);
        logoutButton.setOnClickListener(v -> logoutUser());

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        int id =1;
        Call<loginModel> call = apiService.getsingleuser(id);  // Pass the user ID as an argument

        call.enqueue(new Callback<loginModel>() {
            @Override
            public void onResponse(Call<loginModel> call, Response<loginModel> response) {
                if (response.isSuccessful()) {
                    loginModel user = response.body();
                    Log.e("datat", response.body().getName().getFirstname());

                    firstname.setText(response.body().getName().getFirstname());
                    lastname.setText(response.body().getName().getLastname());
                    email.setText(response.body().getEmail());
                    username.setText(response.body().getUsername());
                    password.setText(response.body().getPassword());
                    city.setText(response.body().getAddress().getCity());
                    street.setText(response.body().getAddress().getStreet());
                    number.setText(response.body().getAddress().getNumber());
                    zipcode.setText(response.body().getAddress().getZipcode());
                    latitude.setText(response.body().getAddress().getGeolocation().getLat());
                    longitude.setText(response.body().getAddress().getGeolocation().getLon());
                    phone.setText(response.body().getPhone());


                    SharedPreferences sharedpreferences = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("firstname", response.body().getName().getFirstname());
                    editor.putString("lastname", response.body().getName().getLastname());
                    editor.putString("email", response.body().getEmail());
                    editor.putString("username", response.body().getUsername());
                    editor.putString("password", response.body().getPassword());
                    editor.putString("city", response.body().getAddress().getCity());
                    editor.putString("street", response.body().getAddress().getStreet());
                    editor.putString("number", String.valueOf(response.body().getAddress().getNumber()));
                    editor.putString("zipcode", response.body().getAddress().getZipcode());
                    editor.putString("latitude", response.body().getAddress().getGeolocation().getLat());
                    editor.putString("longitude", response.body().getAddress().getGeolocation().getLon());
                    editor.putString("phone", response.body().getPhone());

                    editor.apply();

                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<loginModel> call, Throwable t) {
                // Handle network error
            }
        });


        // Uncomment the following line if you have these views in your layout
        // firstTextView = view.findViewById(R.id.your_first_text_view_id);
        // secondTextView = view.findViewById(R.id.your_second_text_view_id);

        // Uncomment the following line if you want to fetch user info
        // fetchUserInfo();

        return view;
    }
    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all preferences
        editor.clear();
        editor.apply();

        // Navigate to the login page
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();  // Close the WelcomePage activity
    }

//    private void clearUserData() {
//        SharedPreferences sharedpreferences = getContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.remove("token");
//        editor.apply();
//        navigateToLoginActivity();
//    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        Toast.makeText(getContext(), "Logout successful", Toast.LENGTH_LONG).show();
        startActivity(intent);
        getActivity().finish();
    }
}
