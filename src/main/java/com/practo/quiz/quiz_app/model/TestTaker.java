package com.practo.quiz.quiz_app.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class TestTaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;  // Test taker (Participant)

    @ManyToOne
    private Test test;  // Test being taken by the user

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> answers; // Answers to the questions (corresponds to the questions list in Test)

    private boolean submitted = false; // Whether the test has been submitted

    // Getters and Setters
}
