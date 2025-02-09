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
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "test_taker_id", nullable = false)
    private TestTaker testTaker;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_option", nullable = false)
    private int selectedOption;

    @Column(name = "is_correct", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean correct;

    public Answer(User user, Test test, TestTaker testTaker, Question question, Integer selectedOption, Boolean correct) {
        this.user = user;
        this.test = test;
        this.testTaker = testTaker;
        this.question = question;
        this.selectedOption = selectedOption;
        this.correct = correct != null ? correct : false;
    }


    // Getters and Setters
}
