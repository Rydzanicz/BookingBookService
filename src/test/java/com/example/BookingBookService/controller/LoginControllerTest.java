package com.example.BookingBookService.controller;

import com.example.BookingBookService.controler.LoginController;
import com.example.BookingBookService.controler.LoginRequest;
import com.example.BookingBookService.controler.RegisterRequest;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private LoginRequest loginRequest;
    private UsersEntity userEntity;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        userEntity = new UsersEntity();
        registerRequest = new RegisterRequest();
    }

    @Test
    void shouldReturn400WhenRequestIsNull() {
        // when
        final ResponseEntity<?> response = loginController.login(null);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Invalid request payload"));
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn400WhenLoginIsNull() {
        // given
        loginRequest.setUsername(null);
        loginRequest.setPassword("password123");

        // when
        final ResponseEntity<?> response = loginController.login(loginRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Invalid request payload"));
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn400WhenPasswordIsNull() {
        // given
        loginRequest.setUsername("login123");
        loginRequest.setPassword(null);

        // when
        final ResponseEntity<?> response = loginController.login(loginRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Invalid request payload"));
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn401WhenUserDoesNotExist() {
        // given
        loginRequest.setUsername("nieistniejący");
        loginRequest.setPassword("hasło123");
        when(loginService.findByUsername("nieistniejący")).thenReturn(Optional.empty());

        // when
        final ResponseEntity<?> response = loginController.login(loginRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("{error=Invalid credentials}");
        verify(loginService).findByUsername("nieistniejący");
    }

    @Test
    void shouldReturn401WhenPasswordIsIncorrect() {
        // given
        loginRequest.setUsername("login123");
        loginRequest.setPassword("złeHasło");

        userEntity.setUsername("login123");
        userEntity.setPassword("poprawneHasło");

        when(loginService.findByUsername("login123")).thenReturn(Optional.of(userEntity));

        // when
        final ResponseEntity<?> response = loginController.login(loginRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("{error=Invalid credentials}");
        verify(loginService).findByUsername("login123");
    }

    @Test
    void shouldReturn200WhenCredentialsAreValid() {
        // given
        String login = "login123";
        String password = "poprawneHasło";

        loginRequest.setUsername(login);
        loginRequest.setPassword(password);

        userEntity.setUsername(login);
        userEntity.setPassword(password);

        when(loginService.findByUsername(login)).thenReturn(Optional.of(userEntity));

        // when
        final ResponseEntity<?> response = loginController.login(loginRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Logging successfully");
        verify(loginService).findByUsername(login);
    }

    @Test
    void shouldReturn400WhenRegisterRequestIsNull() {
        // when
        final ResponseEntity<?> response = loginController.register(null);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn400WhenRegisterUsernameIsNull() {
        // given
        registerRequest.setPassword("hasło123");

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn400WhenRegisterPasswordIsNull() {
        // given
        registerRequest.setUsername("testUser");

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(loginService);
    }

    @Test
    void shouldReturn409WhenRegisterUserAlreadyExists() {
        // given
        registerRequest.setUsername("istniejący");
        registerRequest.setPassword("hasło123");
        when(loginService.registerUser(eq("istniejący"), eq("hasło123"), any()))
                .thenThrow(new IllegalStateException("User with the provided name already exists"));

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(loginService).registerUser(eq("istniejący"), eq("hasło123"), any());
    }

    @Test
    void shouldReturn201WhenRegistrationIsSuccessful() {
        // given
        final String username = "nowyUser";
        final String password = "hasło123";
        final Set<String> roles = Set.of("ROLE_USER");

        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setRoles(roles);

        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRoles(roles);

        when(loginService.registerUser(username, password, roles)).thenReturn(userEntity);

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody)
                .containsEntry("message", "User has been registered")
                .containsEntry("username", username);

        verify(loginService).registerUser(username, password, roles);
    }

    @Test
    void shouldReturn500WhenUnexpectedErrorOccursDuringRegistration() {
        // given
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("hasło123");
        when(loginService.registerUser(any(), any(), any()))
                .thenThrow(new RuntimeException("Nieoczekiwany błąd"));

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "An error occurred during registration"));
        verify(loginService).registerUser(eq("testUser"), eq("hasło123"), any());
    }

    @Test
    void shouldRegisterUserWithDefaultRoleWhenRolesNotProvided() {
        // given
        final String username = "nowyUser";
        final String password = "hasło123";
        final Set<String> defaultRoles = Set.of("ROLE_USER");

        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setRoles(null);

        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRoles(defaultRoles);

        when(loginService.registerUser(username, password, defaultRoles))
                .thenReturn(userEntity);

        // when
        final ResponseEntity<?> response = loginController.register(registerRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody)
                .containsEntry("message", "User has been registered")
                .containsEntry("username", username);

        verify(loginService).registerUser(username, password, defaultRoles);
    }
}