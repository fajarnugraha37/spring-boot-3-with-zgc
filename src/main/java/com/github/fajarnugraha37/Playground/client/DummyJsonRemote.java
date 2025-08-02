package com.github.fajarnugraha37.Playground.client;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static com.github.fajarnugraha37.Playground.client.DummyJsonDTO.*;

public interface DummyJsonRemote {
    @GET("/test")
    CompletableFuture<Map<String, Object>> getTestRoute();

    // --- Products ---
    @GET("products")
    Call<PagedResponse<Product>> getAllProducts(@Query("limit") Integer limit, @Query("skip") Integer skip);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @GET("products/search")
    Call<PagedResponse<Product>> searchProducts(@Query("q") String query);

    @GET("products/categories")
    Call<List<ProductCategory>> getProductCategories();

    @GET("products/category/{category}")
    Call<PagedResponse<Product>> getProductsByCategory(@Path("category") String category);

    // --- Carts ---
    @GET("carts")
    Call<PagedResponse<Cart>> getAllCarts(@Query("limit") Integer limit, @Query("skip") Integer skip);

    @GET("carts/user/{userId}")
    Call<PagedResponse<Cart>> getCartsByUser(@Path("userId") int userId);

    // --- Users ---
    @GET("users")
    Call<PagedResponse<User>> getAllUsers(@Query("limit") Integer limit, @Query("skip") Integer skip);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    // --- Auth ---
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    // --- Posts ---
    @GET("posts")
    Call<PagedResponse<Post>> getAllPosts();

    @GET("posts/user/{userId}")
    Call<PagedResponse<Post>> getPostsByUser(@Path("userId") int userId);

    // --- Comments ---
    @GET("comments")
    Call<PagedResponse<Comment>> getAllComments();

    @GET("comments/post/{postId}")
    Call<PagedResponse<Comment>> getCommentsByPost(@Path("postId") int postId);

    // --- Todos ---
    @GET("todos")
    Call<PagedResponse<Todo>> getAllTodos();

    @GET("todos/user/{userId}")
    Call<PagedResponse<Todo>> getTodosByUser(@Path("userId") int userId);

    // --- Quotes ---
    @GET("quotes")
    Call<PagedResponse<Quote>> getAllQuotes();

    @GET("quotes/{id}")
    Call<Quote> getQuoteById(@Path("id") int id);

    @GET("quotes/random")
    Call<Quote> getRandomQuote();

    // --- Recipes ---
    @GET("recipes")
    Call<PagedResponse<Recipe>> getAllRecipes();

    @GET("recipes/{id}")
    Call<Recipe> getRecipeById(@Path("id") int id);
}
