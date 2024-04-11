package com.groceryapp.Adpator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.Models.Cart;
import com.groceryapp.Models.Product;
import com.groceryapp.R;
import com.groceryapp.Retrofitclient.ApiService;
import com.groceryapp.Single_Product_activity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllProducts_Adpator extends RecyclerView.Adapter<AllProducts_Adpator.CartViewHolder> {

    private List<Product> carts;
    private Context context;
    // Variable to store quantity
    private int selectedQuantity = 0;

    private Drawable selectedDrawable;
    private Drawable unselectedDrawable;

    private int clickedPosition = RecyclerView.NO_POSITION;

    public AllProducts_Adpator(Context context) {
        this.context = context;
    }

    public void setCarts(List<Product> carts) {
        this.carts = carts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_wrapper, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product cart = carts.get(position);

        holder.title.setText(cart.getTitle().substring(0, Math.min(cart.getTitle().length(), 20)) + " ...");
        holder.price.setText("Price: " + cart.getPrice());
        holder.rating.setText(convertRatingToStars(cart.getRating()));
        Picasso.get().load(cart.getImage()).into(holder.image);

        selectedDrawable = ContextCompat.getDrawable(context, R.drawable.colour_rectangle_background);
        unselectedDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_background);

        holder.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the Retrofit API here
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiService.BASE_URL) // Adjust the base URL accordingly
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // If the same button is clicked again
                if (clickedPosition == holder.getAdapterPosition()) {
                    // Reset background to unselectedDrawable
                    holder.addbutton.setBackground(unselectedDrawable);
                    Toast.makeText(context, "Product removed from cart", Toast.LENGTH_SHORT).show();
                    clickedPosition = RecyclerView.NO_POSITION; // Reset clicked position
                } else {
                    // Change background to selectedDrawable
                    holder.addbutton.setBackground(selectedDrawable);

                    // If another button was previously clicked, reset its background
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(clickedPosition);
                    }

                    // Update the clicked position
                    clickedPosition = holder.getAdapterPosition();

                    ApiService cartApiService = retrofit.create(ApiService.class);

                    // Create a Cart object based on the clicked product
                    Cart cartToAdd = new Cart();

                    // Accessing userId
                    SharedPreferences sharedpreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                    int userId = sharedpreferences.getInt("userID", -1);

                    // Accessing the list of products
                    List<Cart.Product> products = new ArrayList<>();

                    // Get the clicked product
                    Product clickedProduct = carts.get(clickedPosition);

                    // Create a new Cart.Product object for the clicked product
                    Cart.Product cartProduct = new Cart.Product();

                    // Set productId and quantity for the Cart.Product object
                    cartProduct.setProductId(clickedProduct.getId());
                    cartProduct.setQuantity(selectedQuantity); // Use the selectedQuantity variable

                    // Add the Cart.Product object to the list of products in Cart
                    products.add(cartProduct);

                    // Set necessary properties of cartToAdd using 'cart' properties
                    cartToAdd.setDate(getCurrentDate());
                    cartToAdd.setUserId(userId);
                    cartToAdd.setProducts(products);

                    Call<Cart> call = cartApiService.addcart(cartToAdd);

                    call.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()) {
                                Cart addedCart = response.body();
                                Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle the error
                                Toast.makeText(context, "Failed to add Product", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {
                            // Handle the failure
                            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }


    @Override
    public int getItemCount() {
        return carts != null ? carts.size() : 0;
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





    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        TextView rating;
        ImageView image;
        RelativeLayout layout;
        TextView quantityTextView;
        TextView plusButton;
        TextView minusButton;
        int quantity = 1;
        TextView addbutton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            addbutton = itemView.findViewById(R.id.addbutton);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product clickedProduct = carts.get(position);

//                        Toast.makeText(context, clickedProduct.getTitle(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, Single_Product_activity.class);
                        intent.putExtra("productId", clickedProduct.getId());
                        context.startActivity(intent);
                    }
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    updateQuantityTextView();
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 0) {
                        quantity--;
                        updateQuantityTextView();
                    }
                }
            });
        }

        private void updateQuantityTextView() {
            quantityTextView.setText(String.valueOf(quantity));
        }
    }
}
