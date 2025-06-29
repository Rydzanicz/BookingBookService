package com.example.BookingBookService.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    @Value("${ratelimit.capacity}")
    private long capacity;

    @Value("${ratelimit.duration}")
    private String durationConfig;

    private Bandwidth limit;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Duration duration;
        if (durationConfig.endsWith("m")) {
            duration = Duration.ofMinutes(Long.parseLong(durationConfig.replace("m", "")));
        } else if (durationConfig.endsWith("s")) {
            duration = Duration.ofSeconds(Long.parseLong(durationConfig.replace("s", "")));
        } else {
            throw new IllegalArgumentException("Nieobsługiwany format duration: " + durationConfig);
        }
        limit = Bandwidth.classic(capacity, Refill.greedy(capacity, duration));
    }

    public Bucket resolveBucket(String userId) {
        return cache.computeIfAbsent(userId, id ->
                Bucket4j.builder().addLimit(limit).build()
        );
    }
}
