package com.example.BookingBookService.controler;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Sprawdź czy książka jest w kolekcji użytkownika
    @GetMapping("/collection/exists")
    public ResponseEntity<Map<String, Boolean>> checkBookExists(
            @RequestParam String username,
            @RequestParam String googleBookId) {
        try {
            boolean exists = bookService.isBookInUserCollection(username, googleBookId);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("exists", false));
        }
    }

    // Dodaj książkę do kolekcji
    @PostMapping("/collection")
    public ResponseEntity<?> addBookToCollection(@RequestBody Map<String, Object> bookData) {
        try {
            String username = (String) bookData.get("username");
            String googleBookId = (String) bookData.get("googleBookId");
            String title = (String) bookData.get("title");
            String authors = (String) bookData.get("authors");
            String description = (String) bookData.get("description");
            String publishedDate = (String) bookData.get("publishedDate");
            Integer pageCount = (Integer) bookData.get("pageCount");
            String categories = (String) bookData.get("categories");
            String thumbnailUrl = (String) bookData.get("thumbnailUrl");

            ReadBookEntity savedBook = bookService.addBookToCollection(
                    username, googleBookId, title, authors, description,
                    publishedDate, pageCount, categories, thumbnailUrl
            );

            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Pobierz wszystkie książki użytkownika
    @GetMapping("/collection")
    public ResponseEntity<?> getUserCollection(@RequestParam String username) {
        try {
            List<ReadBookEntity> books = bookService.getUserBooks(username);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Usuń książkę z kolekcji
    @DeleteMapping("/collection")
    public ResponseEntity<?> removeBookFromCollection(
            @RequestParam String username,
            @RequestParam String googleBookId) {
        try {
            bookService.removeBookFromCollection(username, googleBookId);
            return ResponseEntity.ok(Map.of("message", "Book removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Pobierz konkretną książkę
    @GetMapping("/collection/book")
    public ResponseEntity<?> getUserBook(
            @RequestParam String username,
            @RequestParam String googleBookId) {
        try {
            Optional<ReadBookEntity> book = bookService.getUserBook(username, googleBookId);
            if (book.isPresent()) {
                return ResponseEntity.ok(book.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Policz książki użytkownika
    @GetMapping("/collection/count")
    public ResponseEntity<?> countUserBooks(@RequestParam String username) {
        try {
            long count = bookService.countUserBooks(username);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Wyszukaj książki po tytule
    @GetMapping("/collection/search/title")
    public ResponseEntity<?> searchBooksByTitle(
            @RequestParam String username,
            @RequestParam String title) {
        try {
            List<ReadBookEntity> books = bookService.searchBooksByTitle(username, title);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Wyszukaj książki po autorze
    @GetMapping("/collection/search/author")
    public ResponseEntity<?> searchBooksByAuthor(
            @RequestParam String username,
            @RequestParam String author) {
        try {
            List<ReadBookEntity> books = bookService.searchBooksByAuthor(username, author);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
