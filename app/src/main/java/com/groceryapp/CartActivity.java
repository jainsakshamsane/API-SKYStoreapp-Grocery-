package com.groceryapp;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.groceryapp.Adapters.CartAdapter;
import com.groceryapp.Models.Cart;
import com.groceryapp.Retrofitclient.ApiService;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnDeleteConfirmationListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ApiService apiService;
    ImageView back;
    TextView proceedToPaymentButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        proceedToPaymentButton = findViewById(R.id.checkout);
        ImageView filterImageView = findViewById(R.id.filterImageView);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterOptions();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(this);
        recyclerView.setAdapter(cartAdapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Set the delete confirmation listener
        cartAdapter.setOnDeleteConfirmationListener(this);
        cartAdapter.setOnDeleteConfirmationListener(new CartAdapter.OnDeleteConfirmationListener() {
            @Override
            public void onDeleteConfirmed(int cartId) {
                // Call the API to delete the cart
                deleteCart(cartId);
            }
        });

        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog successDialog = new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText("Thank you for shopping")
                        .setConfirmText("Proceed");

                // Set the background color of the confirm button
                int buttonColor = ContextCompat.getColor(CartActivity.this, R.color.green);
                successDialog.setConfirmButtonBackgroundColor(buttonColor);

                // Set OnClickListener for the "Okay" button
                successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // Perform navigation to the next page here
                        // For example, you can use an Intent to start a new activity
                        Intent intent = new Intent(CartActivity.this, OrderstatusActivity.class);
                        startActivity(intent);

                        // Close the SweetAlertDialog
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

                successDialog.show();
            }
        });




        fetchUserCarts();
    }

    private void fetchUserCarts() {
        // Fetch user carts dynamically based on the logged-in user ID (replace with your logic)
        int userId = getLoggedInUserId();
        Call<List<Cart>> call = apiService.getUserCarts(userId);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    List<Cart> carts = response.body();
                    cartAdapter.updateData(carts);
                } else {
                    Toast.makeText(CartActivity.this, "Failed to fetch user carts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, "Failed to fetch user carts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPaymentDonePopup() {
        // Inflate the custom layout for the payment done pop-up
        View popupView = getLayoutInflater().inflate(R.layout.payment_done_popup_layout, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set a background drawable with rounded corners for the pop-up window
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set focusable to true to enable interaction with the pop-up window
        popupWindow.setFocusable(true);

        // Show the pop-up at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Find the TextView in the pop-up layout
        TextView tvNextPage = popupView.findViewById(R.id.checkout);

        // Set OnClickListener for the TextView to handle navigation
        tvNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation to the next page here
                // For example, start a new activity
                startActivity(new Intent(CartActivity.this, OrderstatusActivity.class));

                // Dismiss the pop-up if needed
                popupWindow.dismiss();
            }
        });
    }

    private void showPaymentConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment");
        builder.setMessage("Are you sure you want to proceed with the payment?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, show the payment done pop-up
                showPaymentDonePopup();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or handle cancel
            }
        });
        builder.show();
    }
    //    private void showFilterOptions() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Filter Options");
//        builder.setItems(new CharSequence[]{"Limit results", "Sort results", "Date range"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle the selected filter option
//                switch (which) {
//                    case 0:
//                        // Limit results logic
//                        break;
//                    case 1:
//                        // Sort results logic
//                        break;
//                    case 2:
//                        // Date range logic
//                        break;
//                }
//            }
//        });
//        builder.show();
//    }
    private void showFilterOptions() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter_options, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    // Replace this with your logic to get the logged-in user ID
    private int getLoggedInUserId() {
//        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
//        return sharedPreferences.getInt("userID", 0);
        return 1;
    }

    @Override
    public void onDeleteConfirmed(int cartId) {
        // Call the API to delete the cart
        deleteCart(cartId);
    }

    private void deleteCart(int cartId) {
        Call<Void> call = apiService.deleteCart(cartId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cart deleted successfully, refresh the data
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fetchUserCarts();
                        }
                    });
                } else {
                    Toast.makeText(CartActivity.this, "Failed to delete cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, "Failed to delete cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
