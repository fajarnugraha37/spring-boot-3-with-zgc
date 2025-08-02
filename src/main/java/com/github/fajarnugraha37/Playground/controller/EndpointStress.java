package com.github.fajarnugraha37.Playground.controller;

import com.github.fajarnugraha37.Playground.util.CachePool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.github.fajarnugraha37.Playground.util.CommonUtil.*;

@RestController
public class EndpointStress {

    private final CachePool<String, String> cache = new CachePool<>(1000L); // 1 second TTL

    // Constantly hit endpoint, uses cache and simulates IO
    @GetMapping("/constant-hit")
    public String constantHit(@RequestParam(defaultValue = "key") String key) {
        var cached = cache.computeIfAbsent(key, k -> "CachedValue_" + UUID.randomUUID());
        var serialized = simulateSerialization(cached);
        // Simulate IO for cached value as well
        File tempFile = null;
        try {
            tempFile = File.createTempFile("constant-", ".tmp");
            try (var writer = new FileWriter(tempFile)) {
                writer.write(serialized);
            }
            Thread.sleep(30 + random.nextInt(70)); // 30-100ms
            tempFile.delete();
        } catch (IOException | InterruptedException ignored) {}
        return "Constant hit response: " + serialized;
    }

    // Long running process, simulates heavy IO
    @GetMapping("/long-running")
    public String longRunning(@RequestParam(defaultValue = "50") int batchSize) {
        var batch = new ArrayList<String>();
        for (var i = 0; i < batchSize; i++) {
            batch.add("LongProcess_" + i + "_" + UUID.randomUUID());
        }
        var processed = simulateBatchIO(batch); // batch IO simulation
        var json = simulateSerialization(batch); // batch serialization
        try {
            Thread.sleep(2000 + random.nextInt(1000)); // Simulate additional DB/network delay
        } catch (InterruptedException ignored) {
            // Restore interrupted state
        }
        return "Long-running batch processed: " + processed + " items, serialized batch: " + json.length() + " bytes";
    }

    // Rare burst, allocates big objects and simulates heavy IO
    @GetMapping("/rare-burst")
    public String rareBurst(@RequestParam(defaultValue = "100") int mb,
                            @RequestParam(defaultValue = "20") int batchSize) {
        var data = new byte[mb][1024 * 1024];
        var batch = new ArrayList<String>();
        for (var i = 0; i < mb; i++) {
            Arrays.fill(data[i], (byte) i);
            batch.add(Base64.getEncoder().encodeToString(data[i]));
        }
        // Simulate batch IO and serialization
        var processed = simulateBatchIO(batch.subList(0, Math.min(batchSize, batch.size())));
        var json = simulateSerialization(batch.subList(0, Math.min(batchSize, batch.size())));
        try {
            Thread.sleep(3000 + random.nextInt(2000)); // Simulate network/IO burst
        } catch (InterruptedException ignored) {
            // Restore interrupted state
        }
        return "Rare burst: Allocated " + mb + " MB, batch processed: " + processed + ", batch serialization size: " + json.length();
    }
}
