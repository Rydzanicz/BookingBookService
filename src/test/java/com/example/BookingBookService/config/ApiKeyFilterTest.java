package com.example.BookingBookService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class ApiKeyFilterTest {

    private ApiKeyFilter apiKeyFilter;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private FilterChain mockFilterChain;

    @BeforeEach
    void setUp() {
        apiKeyFilter = new ApiKeyFilter();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockFilterChain = mock(FilterChain.class);
        apiKeyFilter.setApiKey("VALID_API_KEY");
        apiKeyFilter.setRole("VIGG0=");
    }

    @Test
    void testDoFilter_ValidApiKey() throws IOException, ServletException {
        // Given
        when(mockRequest.getHeader("X-API-KEY")).thenReturn("VIGG0=VALID_API_KEY");
        when(mockResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        // When
        apiKeyFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Then
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        verify(mockResponse, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testDoFilter_MissingHeader() throws IOException, ServletException {
        // Given
        final PrintWriter mockWriter = mock(PrintWriter.class);
        when(mockResponse.getWriter()).thenReturn(mockWriter);
        when(mockRequest.getHeader("X-API-KEY")).thenReturn(null);

        // When
        apiKeyFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Then
        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write("Unauthorized: Invalid or missing API Key");
        verify(mockFilterChain, never()).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testDoFilter_InvalidApiKey() throws IOException, ServletException {
        // Given
        final PrintWriter mockWriter = mock(PrintWriter.class);
        when(mockResponse.getWriter()).thenReturn(mockWriter);
        when(mockRequest.getHeader("X-API-KEY")).thenReturn("ROLE=INVALID_API_KEY");

        // When
        apiKeyFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Then
        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write("Unauthorized: Invalid API Key");
        verify(mockFilterChain, never()).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testDoFilter_EmptyApiKey() throws IOException, ServletException {
        // Given
        final PrintWriter mockWriter = mock(PrintWriter.class);
        when(mockResponse.getWriter()).thenReturn(mockWriter);
        when(mockRequest.getHeader("X-API-KEY")).thenReturn("");

        // When
        apiKeyFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Then
        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write("Unauthorized: Invalid or missing API Key");
        verify(mockFilterChain, never()).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testDoFilter_InternalErrorHandling() throws IOException, ServletException {
        // Given
        final PrintWriter mockWriter = mock(PrintWriter.class);
        when(mockResponse.getWriter()).thenReturn(mockWriter);
        when(mockRequest.getHeader("X-API-KEY")).thenThrow(new RuntimeException("Unexpected error"));

        // When
        apiKeyFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Then
        verify(mockResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(mockWriter).write("Internal Server Error: Unexpected error");
        verify(mockFilterChain, never()).doFilter(mockRequest, mockResponse);
    }
}