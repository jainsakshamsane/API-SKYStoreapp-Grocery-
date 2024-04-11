package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.groceryapp.Adpator.category_singleproduct_Adpator;
import com.groceryapp.Models.Category;
import com.groceryapp.Models.Product;
import com.groceryapp.Retrofitclient.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Single_Category_acitivity extends AppCompatActivity {
    TextView category;
    ImageView back;
    private RecyclerView recyclerView;
    private category_singleproduct_Adpator adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorywise_product_activity);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Single_Category_acitivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        category = findViewById(R.id.category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        adapter = new category_singleproduct_Adpator(this);
        recyclerView.setAdapter(adapter);

        // Fetch category and products
        fetchDataAndSetAdapter();
    }

    private void fetchDataAndSetAdapter() {
        // Retrieve the selected category from the intent
        String selectedCategory = getIntent().getStringExtra("category");

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API call to fetch products for the obtained category
        Call<List<Product>> productsCall = apiService.getCategoryProducts(selectedCategory);

        // Execute the call asynchronously
        productsCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response
                    List<Product> productList = response.body();

                    // Set the fetched data to the adapter
                    if (productList != null) {
                        adapter.setCarts(productList);

                        // Set the category text
                        category.setText(selectedCategory);
                    }
                } else {
                    // Handle error response
                    // For example, you can check response.code() to get the HTTP status code
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle failure
                // For example, network error or parsing error
            }
        });
    }
}
