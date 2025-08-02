package com.github.fajarnugraha37.Playground.util;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

@Slf4j
public class RetrofitUtil {
    private RetrofitUtil() {
        // Prevent instantiation
    }

    public static <T> T invokeCall(Call<T> caller) {
        try {

            var response = caller.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                try (var error = response.errorBody()) {
                    if (error != null) {
                        try {
                            log.error("Error response: {}", error.string());
                            throw new RuntimeException("Failed to read error response: " + error.string());
                        } catch (Exception e) {
                            log.error("Failed to read error response", e);
                            throw new RuntimeException("Failed to read error response", e);
                        }
                    } else {
                        log.error("Request failed with status code: {}", response.code());
                        throw new RuntimeException("Request failed: " + response.message());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Request failed", e);
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }
}
