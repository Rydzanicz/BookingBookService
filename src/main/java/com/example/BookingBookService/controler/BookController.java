package com.example.BookingBookService.controler;

import com.example.BookingBookService.model.User;
import com.example.BookingBookService.model.UserBook;
import com.example.BookingBookService.repository.UserRepository;
import com.example.BookingBookService.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/collection")
    public ResponseEntity<?> getUserCollection(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Set<UserBook> userBooks = user.getUserBooks();
            return ResponseEntity.ok(userBooks);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving collection: " + e.getMessage());
        }
    }

    @PostMapping("/collection")
    public ResponseEntity<?> addToCollection(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody String bookData) {
        try {
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Logika dodawania książki do kolekcji - do rozszerzenia
            return ResponseEntity.ok("Book added to collection");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding book: " + e.getMessage());
        }
    }

    @DeleteMapping("/collection/{bookId}")
    public ResponseEntity<?> removeFromCollection(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable String bookId) {
        try {
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Logika usuwania książki z kolekcji - do rozszerzenia
            return ResponseEntity.ok("Book removed from collection");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing book: " + e.getMessage());
        }
    }
}
