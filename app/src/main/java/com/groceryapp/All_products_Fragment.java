package com.groceryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.Adpator.AllProducts_Adpator;
import com.groceryapp.Adpator.AllProducts_Adpator;
import com.groceryapp.Models.Product;
import com.groceryapp.Retrofitclient.ApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class All_products_Fragment extends Fragment {
    private static final String TAG = "AllProductsFragment";

    RecyclerView recyclerView;
    ImageView sort;
    private AllProducts_Adpator allProductsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_products, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Assuming you are inside a fragment
        allProductsAdapter = new AllProducts_Adpator(requireContext());
        recyclerView.setAdapter(allProductsAdapter);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // API call to fetch initial products
        Call<List<Product>> call = apiService.getproducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    // Log the number of products received
                    Log.d(TAG, "Number of products received: " + (products != null ? products.size() : 0));
                    // Update the RecyclerView data
                    allProductsAdapter.setCarts(products);
                } else {
                    // Handle error
                    Log.e(TAG, "Error response code: " + response.code());
                    try {
                        Log.e(TAG, "Error response: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle failure
                Log.e(TAG, "Error fetching data: " + t.getMessage());
            }
        });

        // Sort ImageView click listener
        sort = view.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the API to fetch sorted products
                Call<List<Product>> sortedProductsCall = apiService.getSortedProducts();
                sortedProductsCall.enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            List<Product> sortedProducts = response.body();
                            // Log the number of sorted products received
                            Log.d(TAG, "Number of sorted products received: " + (sortedProducts != null ? sortedProducts.size() : 0));
                            // Update the RecyclerView data with sorted products
                            allProductsAdapter.setCarts(sortedProducts);
                        } else {
                            // Handle error
                            Log.e(TAG, "Error response code for sorted products: " + response.code());
                            try {
                                Log.e(TAG, "Error response for sorted products: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        // Handle failure
                        Log.e(TAG, "Error fetching sorted products: " + t.getMessage());
                    }
                });
            }
        });

        return view;
    }
}
