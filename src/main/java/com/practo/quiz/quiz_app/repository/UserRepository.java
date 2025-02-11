package com.practo.quiz.quiz_app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.practo.quiz.quiz_app.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // To find user by username
    List<User> findByRole(String role);
}
