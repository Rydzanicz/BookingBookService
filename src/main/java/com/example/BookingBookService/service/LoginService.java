package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    public UsersEntity registerUser(final String username, final String password, final Set<String> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("User with the provided name already exists");
        }

        final UsersEntity newUser = new UsersEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }
}