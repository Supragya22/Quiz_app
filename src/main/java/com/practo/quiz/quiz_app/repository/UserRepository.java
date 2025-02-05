package com.practo.quiz.quiz_app.repository;

import com.yourcompany.quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // To find user by username
}
