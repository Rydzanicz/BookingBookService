package com.example.BookingBookService.service;

import com.example.BookingBookService.controler.request.AddBookRequest;
import com.example.BookingBookService.entity.UserBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserBookRepository;
import com.example.BookingBookService.repository.UsersEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {

    @Mock
    private UserBookRepository userBookRepository;

    @Mock
    private UsersEntityRepository usersEntityRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldAddBookToCollectionSuccess() {
        // Given
        final AddBookRequest request = new AddBookRequest("123", "Test Title", "testUser", "Test Authors", "Test Description");
        final UsersEntity user = new UsersEntity();
        user.setUsername("testUser");

        final UserBookEntity savedBook = new UserBookEntity(user,
                                                            request.getGoogleBookId(),
                                                            request.getTitle(),
                                                            request.getAuthors(),
                                                            request.getDescription());

        when(usersEntityRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.existsByUserAndGoogleBookId(user, "123")).thenReturn(false);
        when(userBookRepository.save(any(UserBookEntity.class))).thenReturn(savedBook);

        // When
        final UserBookEntity result = bookService.addBookToCollection(request);

        // Then
        assertEquals("123", result.getGoogleBookId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Authors", result.getAuthors());
        assertEquals("Test Description", result.getDescription());
        verify(userBookRepository, times(1)).save(any(UserBookEntity.class));
    }

    @Test
    void ShouldAddBookToCollectionUserNotFound() {
        //Given
        final AddBookRequest request = new AddBookRequest("123", "Test Title", "nonExistentUser", "Test Authors", "Test Description");

        when(usersEntityRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // When
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.addBookToCollection(request));

        // Then
        assertEquals("User not found: nonExistentUser", exception.getMessage());
        verify(userBookRepository, never()).save(any(UserBookEntity.class));
    }

    @Test
    void ShouldAddBookToCollectionBookAlreadyExists() {
        //Given
        final AddBookRequest request = new AddBookRequest("123", "Test Title", "testUser", "Test Authors", "Test Description");
        final UsersEntity user = new UsersEntity();
        user.setUsername("testUser");

        when(usersEntityRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.existsByUserAndGoogleBookId(user, "123")).thenReturn(true);

        // When
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.addBookToCollection(request));

        // Then
        assertEquals("Book already exists in user collection", exception.getMessage());
        verify(userBookRepository, never()).save(any(UserBookEntity.class));
    }


    @Test
    void ShouldGetUserBooksSuccess() {
        // Given
        final String username = "testUser";
        final UsersEntity user = new UsersEntity();
        user.setUsername(username);

        final UserBookEntity book1 = new UserBookEntity(user, "123", "Book 1", "authors", "description");
        final UserBookEntity book2 = new UserBookEntity(user, "456", "Book 2", "authors", "description");

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userBookRepository.findByUser(user)).thenReturn(List.of(book1, book2));

        // When
        final List<UserBookEntity> result = bookService.getUserBooks(username);

        // Then
        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getGoogleBookId());
        assertEquals("456", result.get(1).getGoogleBookId());
        verify(userBookRepository, times(1)).findByUser(user);
    }

    @Test
    void ShouldThrowExceptionWhenUserNotFoundOnGetUserBooks() {
        // Given
        final String username = "nonExistentUser";

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.getUserBooks(username));

        // Then
        assertEquals("User not found: nonExistentUser", exception.getMessage());
        verify(userBookRepository, never()).findByUser(any(UsersEntity.class));
    }

    @Test
    void ShouldRemoveBookFromCollectionSuccess() {
        // Given
        final String username = "testUser";
        final String googleBookId = "123";
        final UsersEntity user = new UsersEntity();
        user.setUsername(username);

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        bookService.removeBookFromCollection(username, googleBookId);

        // Then
        verify(userBookRepository, times(1)).deleteByUserAndGoogleBookId(user, googleBookId);
    }

    @Test
    void ShouldThrowExceptionWhenUserNotFoundOnRemoveBookFromCollection() {
        // Given
        final String username = "nonExistentUser";
        final String googleBookId = "123";

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        final RuntimeException exception = assertThrows(RuntimeException.class,
                                                        () -> bookService.removeBookFromCollection(username, googleBookId));

        // Then
        assertEquals("User not found: nonExistentUser", exception.getMessage());
        verify(userBookRepository, never()).deleteByUserAndGoogleBookId(any(UsersEntity.class), anyString());
    }

    @Test
    void ShouldGetUserBookSuccess() {
        // Given
        final String username = "testUser";
        final String googleBookId = "123";
        final UsersEntity user = new UsersEntity();
        user.setUsername(username);
        final UserBookEntity book = new UserBookEntity(user, googleBookId, "Test Book", "authors", "description");

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserAndGoogleBookId(user, googleBookId)).thenReturn(Optional.of(book));

        // When
        final Optional<UserBookEntity> result = bookService.getUserBook(username, googleBookId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("123", result.get().getGoogleBookId());
        assertEquals("Test Book", result.get().getTitle());
        verify(userBookRepository, times(1)).findByUserAndGoogleBookId(user, googleBookId);
    }

    @Test
    void ShouldReturnEmptyWhenUserNotFoundOnGetUserBook() {
        // Given
        final String username = "nonExistentUser";
        final String googleBookId = "123";

        when(usersEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        final Optional<UserBookEntity> result = bookService.getUserBook(username, googleBookId);

        // Then
        assertTrue(result.isEmpty());
        verify(userBookRepository, never()).findByUserAndGoogleBookId(any(UsersEntity.class), anyString());
    }
}