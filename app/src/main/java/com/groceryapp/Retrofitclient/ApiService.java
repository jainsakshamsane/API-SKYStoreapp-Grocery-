package com.groceryapp.Retrofitclient;

import com.groceryapp.Models.Cart;
import com.groceryapp.Models.Category;
import com.groceryapp.Models.Product;
import com.groceryapp.Models.SigninModel;
import com.groceryapp.Models.loginModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "https://fakestoreapi.com/";
    //Kiran
    @GET("products")
    Call<List<Product>> getproducts();
    @GET("products/categories")
    Call<String[]> getCategories();
    @GET("products/{id}")
    Call<Product> getsingleprodct(@Header("Authorization") String authorizationHeader, @Path("id") int itemId);
    @GET("products/category/{category}")
    Call<List<Product>> getCategoryProducts(@Path("category") String category);
    @GET("products?sort=desc")
    Call<List<Product>> getSortedProducts();




    //yashwanth

    @GET("carts/{userId}")
    Call<Cart> getUserCart(@Path("userId") int userId);

    // Fetch a list of user carts and optionally sort them
    @GET("carts")
    Call<List<Cart>> getUserCartsWithSort(@Query("userId") int userId, @Query("sort") String sortOrder);

    // Fetch a list of carts and sort them
    @GET("carts")
    Call<List<Cart>> getCartsSorted(@Query("sort") String sortOrder);

    // Fetch a list of user carts with a specified limit
    @GET("carts")
    Call<List<Cart>> getUserCartsWithLimit(@Query("userId") int userId, @Query("limit") int limit);

    // Delete a cart by ID
    @DELETE("carts/{cartId}")
    Call<Void> deleteCart(@Path("cartId") int cartId);
    @GET("carts/user/{userId}")
    Call<List<Cart>> getUserCarts(@Path("userId") int userId);

    @GET("carts")
    Call<List<Cart>> getUserCartsWithSortAndDateRange(
            @Query("userId") int userId,
            @Query("sort") String sortOrder,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    //saksham
    @POST("users")
    Call<loginModel> register(@Body SigninModel data);   //request - signinModel and response - loginModel

    @POST("auth/login")
    Call<loginModel> login (@Body SigninModel data);
    @GET("users/{id}")
    Call<loginModel> getsingleuser (@Path("id") int userId);

    @PATCH("users/{id}")
    Call<SigninModel> updateUser(@Path("id") int userId, @Body SigninModel request);
    @POST("carts")
    Call<Cart> addcart (@Body Cart cart);

}