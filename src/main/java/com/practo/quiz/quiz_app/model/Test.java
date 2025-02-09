package com.practo.quiz.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor     
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long startTime;

    private Long endTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tests_questions",
            joinColumns = @JoinColumn(name = "tests_id"),
            inverseJoinColumns = @JoinColumn(name = "questions_id")
    )
    private List<Question> questions;

//    @OneToMany(fetch = FetchType.LAZY)
////    @JoinColumn(name = "test_id")
//    private List<Question> questions;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name ="admin_id", nullable=false)
    private User createdBy;
}
