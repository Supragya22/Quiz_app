package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // ✅ Fetch answers given by a test-taker for a specific test
    @Query("SELECT a FROM Answer a WHERE a.testTaker.id = :testTakerId AND a.question.test.id = :testId")
    List<Answer> findByTestTakerIdAndTestId(@Param("testTakerId") Long testTakerId, @Param("testId") Long testId);

    // ✅ Fetch an answer for a specific question and test-taker (to update it)
    @Query("SELECT a FROM Answer a WHERE a.testTaker.id = :testTakerId AND a.question.id = :questionId")
    Optional<Answer> findByTestTakerIdAndQuestionId(@Param("testTakerId") Long testTakerId, @Param("questionId") Long questionId);

    // ✅ Fetch test-taker's score for a given test
    @Query("SELECT COUNT(a) FROM Answer a " +
            "JOIN a.question q " +
            "WHERE a.testTaker.id = :testTakerId " +
            "AND a.test.id = :testId " +
            "AND a.selectedOption = q.correctOptionIndex")
    Integer findScoreByTestTakerIdAndTestId(@Param("testTakerId") Long testTakerId, @Param("testId") Long testId);
}
