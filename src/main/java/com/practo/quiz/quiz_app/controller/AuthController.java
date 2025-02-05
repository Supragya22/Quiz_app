package com.practo.quiz.quiz_app.controller;

import com.yourcompany.quiz.model.User;
import com.yourcompany.quiz.security.JwtUtil;
import com.yourcompany.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Find the user by username
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            // If user exists and passwords match, generate and return JWT token
            return jwtUtil.generateToken(user.getUsername());
        } else {
            return "Invalid credentials!";  // Return error if invalid credentials
        }
    }
}
