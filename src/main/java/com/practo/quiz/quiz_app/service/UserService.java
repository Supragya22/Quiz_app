package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.User;
import com.practo.quiz.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register a new user
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        if ("ROLE_ADMIN".equals(user.getRole())) {
            user.setAuthorities(Collections.singletonList(user.getRole())); // Ensure role is added to authorities
        } else if ("ROLE_TEST_TAKER".equals(user.getRole())) {
            user.setAuthorities(Collections.singletonList(user.getRole())); // Ensure role is added to authorities
        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        return userRepository.save(user);
    }

    // Check if username exists
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Delete a user by ID
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
