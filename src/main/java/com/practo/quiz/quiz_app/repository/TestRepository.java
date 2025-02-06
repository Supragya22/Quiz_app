package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
}
