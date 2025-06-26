package com.example.BookingBookService.service;

import com.example.BookingBookService.model.ERole;
import com.example.BookingBookService.model.User;
import com.example.BookingBookService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validatePassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User createUser(String username, String email, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER);
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    public User registerUser(String username, String email, String password, Set<String> roleStrings) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        Set<ERole> roles = new HashSet<>();

        if (roleStrings == null || roleStrings.isEmpty()) {
            roles.add(ERole.ROLE_USER);
        } else {
            roleStrings.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        roles.add(ERole.ROLE_ADMIN);
                        break;
                    case "mod":
                    case "moderator":
                        roles.add(ERole.ROLE_MODERATOR);
                        break;
                    default:
                        roles.add(ERole.ROLE_USER);
                }
            });
        }

        newUser.setRoles(roles);
        return userRepository.save(newUser);
    }
}
