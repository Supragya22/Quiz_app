package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Get all questions created by a specific admin
    List<Question> findByCreatedById(Long adminId);

    // Get questions by IDs that belong to a specific admin (for assigning to tests)
    @Query("SELECT q FROM Question q WHERE q.id IN :questionIds AND q.createdBy.id = :adminId")
    List<Question> findByIdsAndAdminId(@Param("questionIds") List<Long> questionIds, @Param("adminId") Long adminId);

    @Query("SELECT q FROM Question q JOIN q.tests t WHERE t.id = :testId")
    List<Question> findByTestId(@Param("testId") Long testId);

}
