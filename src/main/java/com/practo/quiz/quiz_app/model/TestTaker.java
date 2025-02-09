package com.practo.quiz.quiz_app.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "testTakers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "test_taker_answers", joinColumns = @JoinColumn(name = "test_taker_id"))
//    @Column(name = "answer")
//    @OneToMany(mappedBy = "testTaker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Integer> answers;

    private boolean submitted;
}
