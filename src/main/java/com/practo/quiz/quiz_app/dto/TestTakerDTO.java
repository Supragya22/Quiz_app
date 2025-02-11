package com.practo.quiz.quiz_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practo.quiz.quiz_app.model.Test;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestTakerDTO {

    private Long test;

    private Long userId;

    private Integer score;

    private Boolean submitted;

}
