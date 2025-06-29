package com.example.BookingBookService.config;

import com.example.BookingBookService.security.RateLimitInterceptor;
import com.example.BookingBookService.service.RateLimitService;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RateLimitInterceptorUnitTest {

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private RateLimitInterceptor interceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void shouldAllowRequestWhenTokensAvailable() throws Exception {
        // Given
        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(true);
        when(rateLimitService.resolveBucket(anyString())).thenReturn(bucket);

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertThat(result).isTrue();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldBlockRequestWhenNoTokensAvailable() throws Exception {
        // Given
        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(false);
        when(rateLimitService.resolveBucket(anyString())).thenReturn(bucket);

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
        assertThat(response.getHeader("Retry-After")).isEqualTo("60");
        assertThat(response.getContentAsString()).isEqualTo("TOO MANY REQUESTS");
    }

    @Test
    void shouldUseUserPrincipalNameWhenAuthenticated() throws Exception {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", null);
        request.setUserPrincipal(auth);

        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(true);
        when(rateLimitService.resolveBucket("testUser")).thenReturn(bucket);

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldUseAnonymousWhenNotAuthenticated() throws Exception {
        // Given
        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(true);
        when(rateLimitService.resolveBucket("anonymous")).thenReturn(bucket);

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldSetCorrectResponseHeadersWhenBlocked() throws Exception {
        // Given
        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(false);
        when(rateLimitService.resolveBucket(anyString())).thenReturn(bucket);

        // When
        interceptor.preHandle(request, response, null);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
        assertThat(response.getHeader("Retry-After")).isEqualTo("60");
        assertThat(response.getContentAsString()).isEqualTo("TOO MANY REQUESTS");
    }

    @Test
    void shouldHandleMultipleRequestsFromSameUser() throws Exception {
        // Given
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.resolveBucket("testUser")).thenReturn(bucket);

        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", null);
        request.setUserPrincipal(auth);

        // When
        when(bucket.tryConsume(1)).thenReturn(true, true, false);

        // Then
        assertThat(interceptor.preHandle(request, response, null)).isTrue();
        assertThat(interceptor.preHandle(request, response, null)).isTrue();
        assertThat(interceptor.preHandle(request, response, null)).isFalse();
    }
}