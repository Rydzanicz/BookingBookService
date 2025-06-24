package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UsersEntity> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}