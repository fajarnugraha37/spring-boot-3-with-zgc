package com.github.fajarnugraha37.Playground.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    public OkHttpClient httpClient(Dispatcher dispatcher,
                                   ConnectionPool connectionPool,
                                   Cache cache,
                                   CookieJar cookieJar) {
        var client = new okhttp3.OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .cookieJar(cookieJar)
                .cache(cache)
                .dns(hostname -> Dns.SYSTEM.lookup(hostname)) // override if needed
                .callTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                .connectTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
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
        dispatcher.setMaxRequests(256);
        dispatcher.setMaxRequestsPerHost(256);

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

    @Bean
    public CookieJar cookieJar() {
        return new CookieJar() {
            private final File cookieDir = new File("cookies");

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                // Persist cookies to file
                try {
                    if (!cookieDir.exists()) cookieDir.mkdirs();
                    var file = new File(cookieDir, url.host() + ".cookies");
                    try (var out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file))) {
                        out.writeObject(new ArrayList<>(cookies));
                    }
                } catch (Exception e) {
                    log.error("Failed to save cookies", e);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                // Load cookies from file
                var file = new File(cookieDir, url.host() + ".cookies");
                if (file.exists()) {
                    try (var in = new java.io.ObjectInputStream(new java.io.FileInputStream(file))) {
                        var obj = in.readObject();
                        if (obj instanceof List) {
                            @SuppressWarnings("unchecked")
                            var cookies = (List<Cookie>) obj;
                            log.info("Loaded {} cookies for {}", cookies.size(), url.host());
                            return cookies;
                        }
                    } catch (Exception e) {
                        log.error("Failed to load cookies", e);
                    }
                }

                return new ArrayList<>();
            }
        };
    }
}
