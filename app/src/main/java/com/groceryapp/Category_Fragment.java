package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.groceryapp.Retrofitclient.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Category_Fragment extends Fragment {
    private TextView electronicsTextView, jewelryTextView, mensClothingTextView, womensClothingTextView;
    private ImageView card1,card2,card3,card4;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        // Initialize TextViews
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);
        electronicsTextView = view.findViewById(R.id.electronics);
        jewelryTextView = view.findViewById(R.id.jewelry);
        mensClothingTextView = view.findViewById(R.id.Mens);
        womensClothingTextView = view.findViewById(R.id.women);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // API call
        Call<String[]> call = apiService.getCategories();

        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                if (response.isSuccessful()) {
                    String[] categories = response.body();
                    if (categories != null && categories.length == 4) {
                        // Log the categories
                        for (String category : categories) {
                            Log.d("Category_Fragment", "Category: " + category);
                        }

                        // Update TextViews with category names
                        updateTextViews(categories);
                    } else {
                        // Handle null response or unexpected size
                        Log.e("Category_Fragment", "Invalid categories response");
                    }
                } else {
                    // Handle error
                    Log.e("Category_Fragment", "Error response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                // Handle failure
                Log.e("Category_Fragment", "Error fetching data: " + t.getMessage());
                t.printStackTrace();  // Log the stack trace for more details
            }
        });

        // Set click listeners for each category
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCategory("electronics");
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCategory("jewelry");
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCategory("men's clothing");
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCategory("women's clothing");
            }
        });

        return view;
    }

    private void updateTextViews(String[] categories) {
        // Ensure categories are not null and have the expected size
        if (categories != null && categories.length == 4) {
            electronicsTextView.setText(categories[0]);
            jewelryTextView.setText(categories[1]);
            mensClothingTextView.setText(categories[2]);
            womensClothingTextView.setText(categories[3]);
        }
    }

    private void navigateToCategory(String category) {
        // Start a new activity or fragment for the selected category
        Intent intent = new Intent(getActivity(), Single_Category_acitivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
