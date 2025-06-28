package com.example.BookingBookService.service;

import com.example.BookingBookService.controler.BookController;
import com.example.BookingBookService.controler.request.AddBookRequest;
import com.example.BookingBookService.controler.request.RemoveBookRequest;
import com.example.BookingBookService.entity.UserBookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldReturnBadRequestWhenAddBookToCollectionFails() {
        // Given
        final AddBookRequest request = new AddBookRequest("adam", "123", "Test Title", "Author", "A sample description");

        when(bookService.addBookToCollection(request)).thenThrow(new RuntimeException("User not found"));

        // When
        final ResponseEntity<?> response = bookController.addBookToCollection(request);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("User not found", ((Map<?, ?>) response.getBody()).get("error"));
        verify(bookService, times(1)).addBookToCollection(request);
    }

    @Test
    void ShouldAddBookToCollectionSuccessfully() {
        // Given
        final AddBookRequest request = new AddBookRequest("adam",
                "123",
                "Effective Java",
                "Joshua Bloch",
                "A must-read book for Java developers");

        // When
        final ResponseEntity<?> response = bookController.addBookToCollection(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("Book added successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void ShouldReturnUserCollectionSuccessfully() {
        // Given
        final String username = "adam";
        final List<UserBookEntity> books = List.of(new UserBookEntity(null, "123", "Test Book 1", "Author", "Description 1"),
                new UserBookEntity(null, "456", "Test Book 2", "Author", "Description 2"));

        when(bookService.getUserBooks(username)).thenReturn(books);

        // When
        final ResponseEntity<?> response = bookController.getUserCollection(username);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(books, response.getBody());
        verify(bookService, times(1)).getUserBooks(username);
    }

    @Test
    void ShouldReturnBadRequestWhenGetUserCollectionFails() {
        // Given
        final String username = "nonExistentUser";

        when(bookService.getUserBooks(username)).thenThrow(new RuntimeException("User not found"));

        // When
        final ResponseEntity<?> response = bookController.getUserCollection(username);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("User not found", ((Map<?, ?>) response.getBody()).get("error"));
        verify(bookService, times(1)).getUserBooks(username);
    }

    @Test
    void ShouldRemoveBookFromCollectionSuccessfully() {
        // Given
        final RemoveBookRequest request = new RemoveBookRequest("adam", "123");

        // When
        final ResponseEntity<?> response = bookController.removeBookFromCollection(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("Book removed successfully", ((Map<?, ?>) response.getBody()).get("message"));
        verify(bookService, times(1)).removeBookFromCollection(request.getUsername(), request.getGoogleBookId());
    }

    @Test
    void ShouldReturnBadRequestWhenRemoveBookFromCollectionFails() {
        // Given
        final RemoveBookRequest request = new RemoveBookRequest("nonExistentUser", "123");

        doThrow(new RuntimeException("User not found")).when(bookService).removeBookFromCollection(request.getUsername(), request.getGoogleBookId());

        // When
        final ResponseEntity<?> response = bookController.removeBookFromCollection(request);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("User not found", ((Map<?, ?>) response.getBody()).get("error"));
        verify(bookService, times(1)).removeBookFromCollection(request.getUsername(), request.getGoogleBookId());
    }
}