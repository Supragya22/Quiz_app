package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Endpoint to create a new question and associate it with a test
    @PostMapping("/test/{testId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long testId, @RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(testId, question);
        if (createdQuestion != null) {
            return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();  // Test not found
    }

    // Endpoint to get all questions for a specific test
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<Question>> getQuestionsByTestId(@PathVariable Long testId) {
        List<Question> questions = questionService.getQuestionsByTestId(testId);
        if (questions != null && !questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.notFound().build();  // Test not found or no questions
    }

    // Endpoint to get all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Endpoint to delete a question by ID
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        boolean isDeleted = questionService.deleteQuestion(questionId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();  // Question not found
    }
}
