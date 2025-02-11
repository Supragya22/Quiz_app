package com.practo.quiz.quiz_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practo.quiz.quiz_app.model.Test;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestTakerDTO {

    private Long id;

    private Test test;

    private Integer score;

}
