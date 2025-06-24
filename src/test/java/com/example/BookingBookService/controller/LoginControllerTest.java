package com.example.BookingBookService.controller;

import com.example.BookingBookService.controler.LoginController;
import com.example.BookingBookService.controler.LoginRequest;
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

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        userEntity = new UsersEntity();
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
        loginRequest.setLogin(null);
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
        loginRequest.setLogin("login123");
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
        loginRequest.setLogin("nieistniejący");
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
        loginRequest.setLogin("login123");
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

        loginRequest.setLogin(login);
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
}