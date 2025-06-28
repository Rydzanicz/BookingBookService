package com.example.BookingBookService.controller;

import com.example.BookingBookService.controler.GoogleBooksController;
import com.example.BookingBookService.model.Book;
import com.example.BookingBookService.service.GoogleBooksService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class GoogleBooksControllerTest {

    private final GoogleBooksService googleBooksService = Mockito.mock(GoogleBooksService.class);
    private final GoogleBooksController googleBooksController = new GoogleBooksController(googleBooksService);

    @Test
    void searchBooksShouldReturnBooksWhenValidRequest() {
        // Given
        final List<Book> mockBooks = List.of(new Book("1", "Test Book 1", List.of("Author 1"), "Description 1", "2023-01-01"),
                                             new Book("2", "Test Book 2", List.of("Author 2"), "Description 2", "2023-02-02"));

        Mockito.when(googleBooksService.searchBooks(anyString(), anyInt())).thenReturn(mockBooks);

        // When
        final ResponseEntity<?> response = googleBooksController.searchBooks("Test", 2);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBooks, response.getBody());
    }

    @Test
    void searchBooksShouldReturnBadRequestWhenServiceThrowsException() {
        // Given
        Mockito.when(googleBooksService.searchBooks(anyString(), anyInt())).thenThrow(new RuntimeException("Service failure"));

        // When
        final ResponseEntity<?> response = googleBooksController.searchBooks("Test", 2);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Service failure", responseBody.get("error"));
    }
}