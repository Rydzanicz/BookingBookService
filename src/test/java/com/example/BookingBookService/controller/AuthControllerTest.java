package com.example.BookingBookService.controller;

import com.example.BookingBookService.controler.AuthController;
import com.example.BookingBookService.dto.JwtResponse;
import com.example.BookingBookService.dto.MessageResponse;
import com.example.BookingBookService.dto.request.LoginRequest;
import com.example.BookingBookService.dto.request.SignupRequest;
import com.example.BookingBookService.model.User;
import com.example.BookingBookService.repository.UserRepository;
import com.example.BookingBookService.security.UserPrincipal;
import com.example.BookingBookService.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUserShouldReturnJwtResponse() {
        // Given
        final LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        final User user = new User("testUser", "testEmail@test.com", "encodedPassword");

        final Authentication authentication = mock(Authentication.class);
        final UserPrincipal userPrincipal = new UserPrincipal(1L, "testUser", "testEmail@test.com", "testPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testJwtToken");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(encoder.matches("testPassword", "encodedPassword")).thenReturn(true);

        // When
        final ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("testJwtToken", jwtResponse.getAccessToken());
        assertEquals(1L, jwtResponse.getId());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerUserShouldReturnMessageResponseWhenUserIsRegisteredSuccessfully() {
        // Given
        final SignupRequest signupRequest = new SignupRequest("newUser", "newEmail@test.com", "newPassword");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("newEmail@test.com")).thenReturn(false);
        when(encoder.encode("newPassword")).thenReturn("encodedPassword");

        // When
        final ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUserShouldReturnErrorWhenUsernameIsTaken() {
        // Given
        final SignupRequest signupRequest = new SignupRequest("existingUser", "newEmail@test.com", "newPassword");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // When
        final ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Username already taken!", messageResponse.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserShouldReturnErrorWhenEmailIsInUse() {
        // Given
        final SignupRequest signupRequest = new SignupRequest("newUser", "existingEmail@test.com", "newPassword");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("existingEmail@test.com")).thenReturn(true);

        // When
        final ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email already in use!", messageResponse.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}