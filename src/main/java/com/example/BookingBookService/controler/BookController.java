package com.example.BookingBookService.controler;

import com.example.BookingBookService.controler.request.AddBookRequest;
import com.example.BookingBookService.controler.request.RemoveBookRequest;
import com.example.BookingBookService.entity.UserBookEntity;
import com.example.BookingBookService.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/collection/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBookToCollection(@RequestBody AddBookRequest addBookRequest) {
        try {

            bookService.addBookToCollection(addBookRequest);
            return ResponseEntity.ok(Map.of("message", "Book added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(value = "/collection")
    public ResponseEntity<?> getUserCollection(@RequestParam(defaultValue = "adam") String username) {
        try {
            final List<UserBookEntity> books = bookService.getUserBooks(username);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping(value = "/collection", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeBookFromCollection(@RequestBody RemoveBookRequest removeBookRequest) {
        try {
            bookService.removeBookFromCollection(removeBookRequest.getUsername(), removeBookRequest.getGoogleBookId());
            return ResponseEntity.ok(Map.of("message", "Book removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
