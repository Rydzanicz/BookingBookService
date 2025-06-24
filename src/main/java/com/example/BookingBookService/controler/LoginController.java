package com.example.BookingBookService.controler;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request payload"));
        }

        final Optional<UsersEntity> user = loginService.findByUsername(loginRequest.getUsername());
        if (user.isPresent() && user.get()
                .getPassword()
                .equals(loginRequest.getPassword())) {

            return ResponseEntity.ok("Logging successfully");
        }

        return ResponseEntity.status(401)
                .body(Map.of("error", "Invalid credentials")
                        .toString());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null ||
                registerRequest.getUsername() == null ||
                registerRequest.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request payload"));
        }

        try {
            final Set<String> roles = registerRequest.getRoles() != null ?
                    registerRequest.getRoles() :
                    Set.of("ROLE_USER");

            final UsersEntity newUser = loginService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    roles
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User has been registered",
                            "username", newUser.getUsername()
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during registration"));
        }
    }
}