package com.github.fajarnugraha37.Playground.client;

import java.util.List;

public class DummyJsonDTO {
    public static class PagedResponse<T> {
        public List<T> products;
        public List<T> carts;
        public List<T> users;
        public List<T> posts;
        public List<T> comments;
        public List<T> todos;
        public List<T> quotes;
        public List<T> recipes;
        public int total;
        public int skip;
        public int limit;
    }

    // Define simplified models
    public static class Product {
        public int id;
        public String title;
        public String description;
    }

    public static class ProductCategory {
        public String slug;
        public String name;
        public String url;
    }

    public static class Cart {
        public int id;
        public List<Product> products;
    }

    public static class User {
        public int id;
        public String username;
        public String email;
    }

    public static class Post {
        public int id;
        public String title;
        public String body;
    }

    public static class Comment {
        public int id;
        public String body;
    }

    public static class Todo {
        public int id;
        public String todo;
        public boolean completed;
    }

    public static class Quote {
        public int id;
        public String quote;
        public String author;
    }

    public static class Recipe {
        public int id;
        public String name;
        public List<String> ingredients;
    }

    public static class AuthResponse {
        public String token;
        public int id;
        public String username;
    }

    // Auth
    public static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
