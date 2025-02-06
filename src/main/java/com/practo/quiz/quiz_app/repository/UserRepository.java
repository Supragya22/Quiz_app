package com.practo.quiz.quiz_app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.practo.quiz.quiz_app.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // To find user by username
}
