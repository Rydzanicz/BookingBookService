package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UsersEntity> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}