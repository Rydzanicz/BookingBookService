package com.example.BookingBookService.controler;

import com.example.BookingBookService.dto.JwtResponse;
import com.example.BookingBookService.dto.LoginRequest;
import com.example.BookingBookService.dto.MessageResponse;
import com.example.BookingBookService.dto.SignupRequest;
import com.example.BookingBookService.model.RefreshToken;
import com.example.BookingBookService.model.User;
import com.example.BookingBookService.repository.UserRepository;
import com.example.BookingBookService.security.UserPrincipal;
import com.example.BookingBookService.security.jwt.JwtUtils;
import com.example.BookingBookService.service.LoginService;
import com.example.BookingBookService.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final Optional<User> user = loginService.findByUsername(loginRequest.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User not found!"));
        }

        if (!loginService.validatePassword(user.get(), loginRequest.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Invalid credentials!"));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userPrincipal.getId(),
                userPrincipal.getUsername(), userPrincipal.getEmail(), userPrincipal.getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        try {
            final User newUser = loginService.registerUser(
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    signUpRequest.getRole()
            );

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Registration failed - " + e.getMessage()));
        }
    }
}
