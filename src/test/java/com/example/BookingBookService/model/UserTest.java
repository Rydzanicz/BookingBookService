package com.example.BookingBookService.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void ShouldCreateUserWithValidArguments() {
        // Given
        final String username = "testUser";
        final String email = "test@example.com";
        final String password = "password123";

        // When
        final User user = new User(username, email, password);

        // Then
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void ShouldThrowExceptionWhenUsernameIsNull() {
        // Given
        final String email = "test@example.com";
        final String password = "password123";

        // When
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new User(null, email, password));

        // Then
        assertEquals("Username cannot be null or blank", exception.getMessage());
    }

    @Test
    void ShouldThrowExceptionWhenEmailIsNull() {
        // Given
        final String username = "testUser";
        final String password = "password123";

        // When
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new User(username, null, password));

        // Then
        assertEquals("Email cannot be null or blank", exception.getMessage());
    }

    @Test
    void ShouldThrowExceptionWhenPasswordIsNull() {
        // Given
        final String username = "testUser";
        final String email = "test@example.com";

        // When
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new User(username, email, null));

        // Then
        assertEquals("Password cannot be null or blank", exception.getMessage());
    }
    @Test
    void ShouldCreateUserWithDefaultConstructor() {
        // When
        final User user = new User();

        // Then
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }
}