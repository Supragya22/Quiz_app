package com.practo.quiz.quiz_app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tests_questions",
            joinColumns = @JoinColumn(name = "tests_id"),
            inverseJoinColumns = @JoinColumn(name = "questions_id")
    )
    private List<Question> questions;


    private boolean isActive;

    @ManyToOne
    @JoinColumn(name ="admin_id", nullable=false)
    private User createdBy;

    public void updateIsActive() {
        LocalDateTime now = LocalDateTime.now();
        this.isActive = (startTime != null && endTime != null) && now.isAfter(startTime) && now.isBefore(endTime);
    }
}
