package com.groceryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.groceryapp.Models.Product;
import com.groceryapp.Retrofitclient.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Single_Product_activity extends AppCompatActivity {

    private ImageView productImage,back;
    TextView rating,buynowbutton;
    private TextView productName, productPrice,productInformation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_singleproduct);

        // Initialize views
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productInformation = findViewById(R.id.productInformation);
        rating = findViewById(R.id.rating);
        back = findViewById(R.id.back);
        buynowbutton = findViewById(R.id.buyNowButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Single_Product_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buynowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Single_Product_activity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        // Retrieve productId from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productId")) {
            int productId = intent.getIntExtra("productId", -1);
            String authorizationHeader = "YOUR_AUTH_TOKEN";
            // Make API call to get single product details
            ApiService apiService = createApiService(); // Create your ApiService instance
            Call<Product> call = apiService.getsingleprodct(authorizationHeader, productId);


            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        // Update UI with product details
                        updateUI(product);
                    } else {
                        // Handle error
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    // Handle failure
                }
            });
        } else {
            // Handle case where productId is not available
        }
    }

    private void updateUI(Product product) {
        // Update UI with product details
        if (product != null) {
            // Use Picasso or Glide to load the image into the ImageView
            Picasso.get().load(product.getImage()).into(productImage);

            // Set product name and price
            productName.setText(product.getTitle());
            productPrice.setText("Price: " + product.getPrice());
            rating.setText(convertRatingToStars(product.getRating()));
            // Set product information with bold heading and different text sizes
            String heading = "Information:";
            String informationText = heading + " " + product.getDescription();

            SpannableString spannableString = new SpannableString(informationText);

            // Set heading as bold
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, heading.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set heading text size
            spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, heading.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set information text size
            spannableString.setSpan(new AbsoluteSizeSpan(16, true), heading.length(), informationText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            productInformation.setText(spannableString);
        }
    }

    private String convertRatingToStars(Product.Rating rating) {
        double rate = Double.parseDouble(rating.getRate());
        int count = Integer.parseInt(rating.getCount());

        // Round up if the decimal part is greater than or equal to 0.5
        int roundedRating = (int) Math.ceil(rate * 2);

        // Ensure the rounded rating is at least 1
        roundedRating = Math.max(1, roundedRating);

        int fullStars = roundedRating / 2;
        int halfStar = (roundedRating % 2 == 1) ? 1 : 0;

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("\u2605"); // Full star
        }

        if (halfStar == 1) {
            double decimalPart = rate - (fullStars * 0.5);
            if (decimalPart > 0.1 && decimalPart < 0.5) {
                stars.append("1\u20442"); // 1/2 star
            } else if (decimalPart >= 0.5 && decimalPart < 0.9) {
                stars.append("\u00BD"); // Half star
            } else {
                stars.append(""); // No additional text for this case
            }
        }

        int emptyStars = 5 - (fullStars + halfStar);
        for (int i = 0; i < emptyStars; i++) {
            stars.append("\u2606"); // Empty star
        }

        stars.append(" (").append(count).append(")");

        return stars.toString();
    }

    // You need to create this method to obtain your ApiService instance
    private ApiService createApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }
}
