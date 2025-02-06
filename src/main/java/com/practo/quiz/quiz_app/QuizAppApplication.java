package com.practo.quiz.quiz_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootApplication
@EnableJpaRepositories(basePackages ="com.practo.quiz.quiz_app.repository")
@ComponentScan(basePackages = {"com.practo.quiz.quiz_app.security", "com.practo.quiz.quiz_app.service","com.practo.quiz.quiz_app.controller"})
@EntityScan(basePackages ="com.practo.quiz.quiz_app.model")  
@EnableAutoConfiguration
public class QuizAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizAppApplication.class, args);
    }
}
