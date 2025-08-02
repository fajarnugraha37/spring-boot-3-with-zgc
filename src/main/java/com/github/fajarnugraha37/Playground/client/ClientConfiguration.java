package com.github.fajarnugraha37.Playground.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
public class ClientConfiguration {

    @Bean
    public DummyJsonRemote dummyJsonRemote(OkHttpClient httpClient) {
        var retrofit = retrofit("https://dummyjson.com", httpClient);
        return retrofit.create(DummyJsonRemote.class);
    }

    public Retrofit retrofit(String FQDN, OkHttpClient httpClient) {
        var retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(FQDN)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create());

        return retrofit.build();
    }

    @Bean
    public OkHttpClient httpClient(Dispatcher dispatcher, ConnectionPool connectionPool, Cache cache) {
        var client = new okhttp3.OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .cache(cache)
                .dns(hostname -> Dns.SYSTEM.lookup(hostname)) // override if needed
                .callTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    var request = chain.request();
                    // Add any custom headers or logging here if needed
                    return chain.proceed(request);
                })
                .addNetworkInterceptor(chain -> {
                    var response = chain.proceed(chain.request());
                    // Add any custom response handling or logging here if needed
                    return response;
                });

        return client.build();
    }

    @Bean
    public Dispatcher dispatcher() {
        var dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(128);
        dispatcher.setMaxRequestsPerHost(128);

        return dispatcher;
    }

    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(100, 5, TimeUnit.MINUTES);
    }

    @Bean
    public Cache cache() {
        // Configure cache with a size of 10 MB and a directory for cache files cwd/cache
        var cacheDir = new File("cache");
        var cacheSize = 10 * 1024 * 1024; // 10 MB

        return new Cache(cacheDir, cacheSize);
    }
}
