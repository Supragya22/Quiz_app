package com.practo.quiz.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    private int correctOptionIndex;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User createdBy;


    @ManyToOne(fetch = FetchType.LAZY) // Set to LAZY to prevent automatic loading
    @JoinColumn(name = "test_id")
    @JsonIgnore  // Prevent infinite recursion
    private Test test;

    @ManyToMany(mappedBy = "questions")
    @JsonBackReference
    @JsonIgnore
    private List<Test> tests;

}
