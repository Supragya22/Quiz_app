package com.practo.quiz.quiz_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestScoreDTO {
    private Long testTakerId;
    private String testTakerName;
    private int score;
}
