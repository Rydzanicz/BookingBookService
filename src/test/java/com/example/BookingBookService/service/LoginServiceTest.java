package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginService = new LoginService(userRepository);
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // given
        final String username = "testUser";
        final UsersEntity expectedUser = new UsersEntity();
        expectedUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // when
        final Optional<UsersEntity> result = loginService.findByUsername(username);

        // then
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // given
        final String username = "nieistniejącyUżytkownik";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        final Optional<UsersEntity> result = loginService.findByUsername(username);

        // then
        assertTrue(result.isEmpty());
        verify(userRepository).findByUsername(username);
    }
}