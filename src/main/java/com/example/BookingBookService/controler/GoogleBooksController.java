package com.example.BookingBookService.controler;

import com.example.BookingBookService.model.Book;
import com.example.BookingBookService.service.GoogleBooksService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/google")
public class GoogleBooksController {

    GoogleBooksService googleBooksService;

    public GoogleBooksController(final GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            final Page<Book> response = googleBooksService.searchBooks(query, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
