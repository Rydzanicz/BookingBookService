package com.example.BookingBookService.service;

import com.example.BookingBookService.exception.TokenRefreshException;
import com.example.BookingBookService.model.RefreshToken;
import com.example.BookingBookService.model.User;
import com.example.BookingBookService.repository.RefreshTokenRepository;
import com.example.BookingBookService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 604800000L);
    }

    @Test
    void findByTokenShouldReturnTokenWhenExists() {
        // Given
        final String tokenValue = "test-token";
        final RefreshToken expectedToken = new RefreshToken();
        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(expectedToken));

        // When
        final Optional<RefreshToken> result = refreshTokenService.findByToken(tokenValue);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedToken, result.get());
        verify(refreshTokenRepository).findByToken(tokenValue);
    }

    @Test
    void findByTokenShouldReturnEmptyWhenNotExists() {
        // Given
        final String tokenValue = "non-existent-token";
        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        // When
        final Optional<RefreshToken> result = refreshTokenService.findByToken(tokenValue);

        // Then
        assertTrue(result.isEmpty());
        verify(refreshTokenRepository).findByToken(tokenValue);
    }

    @Test
    void createRefreshTokenShouldCreateNewToken() {
        // Given
        final Long userId = 1L;
        final User user = new User();
        final RefreshToken savedToken = new RefreshToken();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedToken);

        // When
        final RefreshToken result = refreshTokenService.createRefreshToken(userId);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void verifyExpirationShouldReturnTokenWhenValid() {
        // Given
        final RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(3600));

        // When
        final RefreshToken result = refreshTokenService.verifyExpiration(token);

        // Then
        assertNotNull(result);
        assertEquals(token, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpirationShouldThrowExceptionWhenExpired() {
        // Given
        final RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().minusSeconds(3600));

        // When
        final TokenRefreshException exception = assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.verifyExpiration(token));
        // Then
        assertTrue(exception.getMessage().contains("Refresh token was expired"));
        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByUserIdShouldDeleteTokens() {
        // Given
        final Long userId = 1L;
        final User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.deleteByUser(user)).thenReturn(1);

        // When
        final int deletedCount = refreshTokenService.deleteByUserId(userId);

        // Then
        assertEquals(1, deletedCount);
        verify(userRepository).findById(userId);
        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    void createRefreshTokenShouldSetCorrectExpiryDate() {
        // Given
        final Long userId = 1L;
        final User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        final RefreshToken result = refreshTokenService.createRefreshToken(userId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getExpiryDate());
        assertTrue(result.getExpiryDate().isAfter(Instant.now()));
        assertTrue(result.getExpiryDate().isBefore(Instant.now().plusMillis(604800001L)));
    }
}