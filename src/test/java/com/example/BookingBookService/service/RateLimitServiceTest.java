package com.example.BookingBookService.service;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitServiceTest {

    private RateLimitService rateLimitService;

    @BeforeEach
    void setUp() {
        rateLimitService = new RateLimitService();
        ReflectionTestUtils.setField(rateLimitService, "capacity", 10L);
        ReflectionTestUtils.setField(rateLimitService, "durationConfig", "1m");
    }

    @Test
    void shouldInitializeWithMinuteDuration() {
        // Given
        // When
        rateLimitService.init();

        // Then
        assertNotNull(ReflectionTestUtils.getField(rateLimitService, "limit"));
    }

    @Test
    void shouldInitializeWithSecondsDuration() {
        // Given
        ReflectionTestUtils.setField(rateLimitService, "durationConfig", "30s");

        // When
        rateLimitService.init();

        // Then
        assertNotNull(ReflectionTestUtils.getField(rateLimitService, "limit"));
    }

    @Test
    void shouldThrowExceptionForInvalidDurationFormat() {
        // Given
        ReflectionTestUtils.setField(rateLimitService, "durationConfig", "1h");

        // When
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rateLimitService.init()
        );
        // Then
        assertEquals("Nieobsługiwany format duration: 1h", exception.getMessage());
    }

    @Test
    void shouldCreateNewBucketForNewUser() {
        // Given
        rateLimitService.init();
        String userId = "testUser";

        // When
        Bucket bucket = rateLimitService.resolveBucket(userId);

        // Then
        assertNotNull(bucket);
        assertEquals(10, bucket.getAvailableTokens());
    }

    @Test
    void shouldReuseExistingBucketForSameUser() {
        // Given
        rateLimitService.init();
        String userId = "testUser";

        // When
        Bucket bucket1 = rateLimitService.resolveBucket(userId);
        bucket1.tryConsume(1);
        Bucket bucket2 = rateLimitService.resolveBucket(userId);

        // Then
        assertNotNull(bucket2);
        assertEquals(9, bucket2.getAvailableTokens());
        assertSame(bucket1, bucket2);
    }

    @Test
    void shouldCreateSeparateBucketsForDifferentUsers() {
        // Given
        rateLimitService.init();
        String userId1 = "user1";
        String userId2 = "user2";

        // When
        Bucket bucket1 = rateLimitService.resolveBucket(userId1);
        Bucket bucket2 = rateLimitService.resolveBucket(userId2);

        // Then
        assertNotNull(bucket1);
        assertNotNull(bucket2);
        assertNotSame(bucket1, bucket2);

        // When
        bucket1.tryConsume(1);

        // Then
        assertEquals(9, bucket1.getAvailableTokens());
        assertEquals(10, bucket2.getAvailableTokens());
    }

    @Test
    void shouldRespectCapacityLimit() {
        // Given
        rateLimitService.init();
        String userId = "testUser";
        Bucket bucket = rateLimitService.resolveBucket(userId);

        // When
        for (int i = 0; i < 10; i++) {
            assertTrue(bucket.tryConsume(1));
        }

        // Then
        assertFalse(bucket.tryConsume(1));
        assertEquals(0, bucket.getAvailableTokens());
    }
}