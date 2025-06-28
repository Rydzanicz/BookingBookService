package com.example.BookingBookService.controler;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;


    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/collection/add")
    public ResponseEntity<?> addBookToCollection(@RequestBody Map<String, Object> bookData) {
        try {
            final String username = (String) bookData.get("username");
            final String googleBookId = (String) bookData.get("googleBookId");
            final String title = (String) bookData.get("title");
            final String authors = (String) bookData.get("authors");
            final String description = (String) bookData.get("description");
            final String publishedDate = (String) bookData.get("publishedDate");
            final Integer pageCount = (Integer) bookData.get("pageCount");
            final String categories = (String) bookData.get("categories");
            final String thumbnailUrl = (String) bookData.get("thumbnailUrl");
            final ReadBookEntity savedBook = bookService.addBookToCollection(username,
                                                                             googleBookId,
                                                                             title,
                                                                             authors,
                                                                             description,
                                                                             publishedDate,
                                                                             pageCount,
                                                                             categories,
                                                                             thumbnailUrl);

            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(value = "/collection", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserCollection(@RequestParam String username) {
        try {
            final List<ReadBookEntity> books = bookService.getUserBooks(username);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping(value = "/collection", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeBookFromCollection(@RequestParam String username, @RequestParam String googleBookId) {
        try {
            bookService.removeBookFromCollection(username, googleBookId);
            return ResponseEntity.ok(Map.of("message", "Book removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
