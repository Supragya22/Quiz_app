package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.service.QuestionService;
import com.practo.quiz.quiz_app.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestService testService;

    // Create a new question (no longer associated with a test here)
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    // Get all questions for a specific test (moved logic to TestService)
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<Question>> getQuestionsByTestId(@PathVariable Long testId) {
        List<Question> questions = testService.getQuestionsByTestId(testId);
        if (questions != null && !questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.notFound().build();  // Test not found or no questions
    }

    // Get all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Delete a question by ID
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        boolean isDeleted = questionService.deleteQuestion(questionId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();  // Question not found
    }
}
