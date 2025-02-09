package com.practo.quiz.quiz_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to the test-taker who provided the answer
    @ManyToOne
    @JoinColumn(name = "test_taker_id", nullable = false)
    private TestTaker testTaker;

    // Foreign key to the question for which the answer is given
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // The option selected by the test-taker (1, 2, 3, or 4)
    @Column(nullable = false)
    private int selectedOption;

    // Indicates whether the selected answer is correct or not
    @Column(nullable = false)
    private boolean isCorrect;

}
