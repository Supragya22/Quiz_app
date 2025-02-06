package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.TestTaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTakerRepository extends JpaRepository<TestTaker, Long> {
    TestTaker findByUserIdAndTestId(Long userId, Long testId);
}
