package com.example.BookingBookService.controler;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.service.LoginService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getLogin() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Invalid request payload");
        }

        final Optional<UsersEntity> user = loginService.findByUsername(loginRequest.getLogin());
        if (user.isPresent() && user.get()
                .getPassword()
                .equals(loginRequest.getPassword())) {

            return ResponseEntity.ok("Logging successfully");
        }

        return ResponseEntity.status(401)
                .body(Map.of("error", "Invalid credentials")
                        .toString());
    }
}