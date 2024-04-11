package com.groceryapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.Models.Cart;
import com.groceryapp.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> carts;
    private OnDeleteConfirmationListener onDeleteConfirmationListener;
    private Context context;  // Add this line

    // Constructor to initialize the context
    public CartAdapter(Context context) {
        this.context = context;
        this.carts = new ArrayList<>();
    }

    public CartAdapter() {
        this.carts = new ArrayList<>();
    }
    public interface OnDeleteConfirmationListener {
        void onDeleteConfirmed(int cartId);
    }

    public void setOnDeleteConfirmationListener(OnDeleteConfirmationListener listener) {
        this.onDeleteConfirmationListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartIdTextView;
        TextView dateTextView;
        LinearLayout productLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartIdTextView = itemView.findViewById(R.id.cartIdTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            productLayout = itemView.findViewById(R.id.productLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = carts.get(position);
        Log.d("CartAdapter", "onBindViewHolder - Number of products in cart " + cart.getId() + ": " + cart.getProducts().size());

        holder.cartIdTextView.setText("Cart ID: " + cart.getId());
        holder.dateTextView.setText("Date: " + cart.getDate());

        holder.productLayout.removeAllViews(); // Clear previous product views

        if (holder.productLayout != null) {
            for (Cart.Product product : cart.getProducts()) {
                View productView = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.item_product, holder.productLayout, false);

                TextView productIdTextView = productView.findViewById(R.id.productIdTextView);
                TextView increaseQuantityTextView = productView.findViewById(R.id.increaseQuantityTextView);
                TextView quantityTextView = productView.findViewById(R.id.quantityTextView);
                TextView decreaseQuantityTextView = productView.findViewById(R.id.decreaseQuantityTextView);
                ImageView deleteImageView = productView.findViewById(R.id.deleteImageView);

                productIdTextView.setText("Product ID: " + product.getProductId());
                quantityTextView.setText(" " + product.getQuantity());

                increaseQuantityTextView.setOnClickListener(view -> {
                    int newQuantity = product.getQuantity() + 1;
                    product.setQuantity(newQuantity);
                    notifyItemChanged(holder.getAdapterPosition());
                });

                decreaseQuantityTextView.setOnClickListener(view -> {
                    int newQuantity = Math.max(0, product.getQuantity() - 1);
                    product.setQuantity(newQuantity);
                    notifyItemChanged(holder.getAdapterPosition());
                });

                deleteImageView.setOnClickListener(view -> {
                    // Check if the listener is set
                    if (onDeleteConfirmationListener != null) {
                        // Show confirmation dialog
                        showDeleteConfirmationDialog(cart.getId());
                    }
                });

                holder.productLayout.addView(productView);
            }
        }
    }
    private void showDeleteConfirmationDialog(int cartId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this cart?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Call the listener's onDeleteConfirmed method
            onDeleteConfirmationListener.onDeleteConfirmed(cartId);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing or handle cancel
        });
        builder.show();
    }

    @Override
    public int getItemCount() {

        return carts.size();
    }

    public void updateData(List<Cart> carts) {
        this.carts = carts;
        notifyDataSetChanged(); // Remove this line
        Log.d("CartAdapter", "updateData - Number of carts: " + carts.size());
    }
}


