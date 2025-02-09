package com.practo.quiz.quiz_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_takers")
public class TestTaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Test-taker

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    private boolean submitted; // Tracks if the test is submitted
    private Integer score; // Stores the score

    public TestTaker(User user, Test test) {
        this.user = user;
        this.test = test;
        this.submitted = false;
        this.score = 0;
    }

    // Getters and Setters
}
