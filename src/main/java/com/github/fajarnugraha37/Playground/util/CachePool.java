package com.github.fajarnugraha37.Playground.util;

import java.util.Map;
import java.util.concurrent.*;

public class CachePool<K, V> {
    private final ConcurrentHashMap<K, CacheEntry<V>> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public CachePool(long cleanupIntervalMillis) {
        cleaner.scheduleAtFixedRate(this::cleanup, cleanupIntervalMillis, cleanupIntervalMillis, TimeUnit.MILLISECONDS);
    }

    public void put(K key, V value, long ttlMillis) {
        long expireAt = System.currentTimeMillis() + ttlMillis;
        map.put(key, new CacheEntry<>(value, expireAt));
    }

    public V computeIfAbsent(K key, long ttlMillis, java.util.function.Function<? super K, ? extends V> mappingFunction) {
        CacheEntry<V> entry = map.get(key);
        if (entry == null || entry.isExpired()) {
            V value = mappingFunction.apply(key);
            put(key, value, ttlMillis); // Default TTL of 1 second
            return value;
        }
        return entry.value;
    }

    public V computeIfAbsent(K key, java.util.function.Function<? super K, ? extends V> mappingFunction) {
        return computeIfAbsent(key, 1000L, mappingFunction); // Default TTL of 1 second
    }

    public V get(K key) {
        CacheEntry<V> entry = map.get(key);
        if (entry == null || entry.isExpired()) {
            map.remove(key);
            return null;
        }
        return entry.value;
    }

    public void remove(K key) {
        map.remove(key);
    }

    public int size() {
        return map.size();
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        for (Map.Entry<K, CacheEntry<V>> entry : map.entrySet()) {
            if (entry.getValue().expireAt <= now) {
                map.remove(entry.getKey());
            }
        }
    }

    public void shutdown() {
        cleaner.shutdown();
    }

    private static class CacheEntry<V> {
        final V value;
        final long expireAt;
        CacheEntry(V value, long expireAt) {
            this.value = value;
            this.expireAt = expireAt;
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }
}
