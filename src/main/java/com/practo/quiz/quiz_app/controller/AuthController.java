package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.User;
import com.practo.quiz.quiz_app.security.JwtUtil;
import com.practo.quiz.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Find the user by username
        System.out.println("entering controller");
        System.out.println("user"+user);
        User foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            // Generate JWT token with username and role
            String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getRole());

            // Return token in response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", foundUser.getRole());
            response.put("username", foundUser.getUsername());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
    }
}
