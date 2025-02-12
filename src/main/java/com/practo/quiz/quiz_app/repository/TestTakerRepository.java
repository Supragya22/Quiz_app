package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestTakerRepository extends JpaRepository<TestTaker, Long> {

    Optional<TestTaker> findByUserIdAndTestId(Long userId, Long testId);

    @Query("SELECT tt.test FROM TestTaker tt WHERE tt.user.id = :userId")
    List<Test> findAssignedTestsByUserId(Long userId);

    @Query("SELECT tt FROM TestTaker tt WHERE tt.user.id = :userId AND tt.submitted = false")
    Optional<TestTaker> findOngoingTestByUserId(Long userId);

    @Query("SELECT tt FROM TestTaker tt WHERE tt.test.id = :testId")
    List<TestTaker> findAllByTestId(@Param("testId") Long testId);

}
