package com.github.fajarnugraha37.Playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;
import java.util.Random;

public class CommonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Random random = new Random();

    private CommonUtil() {
        // Prevent instantiation
    }

    // Simulated serialization/deserialization delay
    public static String simulateSerialization(Object obj) {
        try {
            // Serialize to JSON
            var serialized = objectMapper.writeValueAsString(obj);
            // Simulate serialization time
            Thread.sleep(50 + random.nextInt(50)); // 50-100ms
            // Deserialize
            var deserialized = objectMapper.readValue(serialized, Object.class);
            // Simulate deserialization time
            Thread.sleep(50 + random.nextInt(50)); // 50-100ms
            return serialized;
        } catch (Exception e) {
            return "Serialization error";
        }
    }

    // Simulated batched IO (e.g., batch insert/update)
    public static int simulateBatchIO(List<String> batch) {
        var success = 0;
        for (var item : batch) {
            try {
                var tempFile = File.createTempFile("batch-", ".tmp");
                try (var fw = new FileWriter(tempFile)) {
                    fw.write(item);
                }
                Thread.sleep(20 + random.nextInt(30)); // 20-50ms per item
                success++;
                tempFile.delete();
            } catch (IOException | InterruptedException e) {
                // fail silently for simulation
            }
        }
        return success;
    }

    // Simulated IO: writes and reads from a temp file
    public static String simulateIO(String key, String value) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("io-sim-" + key, ".tmp");
            try (var writer = new FileWriter(tempFile)) {
                writer.write(value);
            }
            // Simulate IO-bound latency
            Thread.sleep(100 + new Random().nextInt(200)); // 100-300 ms
            StringBuilder result = new StringBuilder();
            try (var reader = new BufferedReader(new FileReader(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        } catch (IOException | InterruptedException e) {
            return "IO Error";
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

}
