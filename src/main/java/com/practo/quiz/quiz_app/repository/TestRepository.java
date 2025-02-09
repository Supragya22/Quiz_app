package com.practo.quiz.quiz_app.repository;

import com.practo.quiz.quiz_app.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("SELECT t FROM Test t WHERE t.createdBy.id = :adminId")
    List<Test> findByAdminId(@Param("adminId") Long adminId);

}
