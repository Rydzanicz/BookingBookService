package com.example.BookingBookService.controler;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserRepository;
import com.example.BookingBookService.service.BookService;
import com.example.BookingBookService.service.GoogleBooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final GoogleBooksService googleBooksService;

    public BookController(final BookService bookService, final UserRepository userRepository, final GoogleBooksService googleBooksService) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.googleBooksService = googleBooksService;
    }

    @GetMapping(value = "/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReadBooks(@RequestParam String username) {
        try {
            final UsersEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<ReadBookEntity> readBooks = bookService.getUserReadBooks(user);
            return ResponseEntity.ok(readBooks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Could not get read books: " + e.getMessage()));
        }
    }

    @DeleteMapping(value = "/read/{readBookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeFromReadList(@PathVariable Long readBookId,
                                                @RequestParam String username) {
        try {
            final UsersEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            bookService.removeFromReadList(user, readBookId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Could not remove book: " + e.getMessage()));
        }
    }
}