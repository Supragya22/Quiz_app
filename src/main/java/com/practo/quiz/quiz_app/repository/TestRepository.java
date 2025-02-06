package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
