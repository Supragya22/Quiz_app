package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.User;
import com.practo.quiz.quiz_app.repository.UserRepository;
import com.practo.quiz.quiz_app.security.JwtUtil;
import com.practo.quiz.quiz_app.service.QuestionService;
import com.practo.quiz.quiz_app.service.TestService;
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

    @Autowired
    private TestService testService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(@RequestHeader("Authorization") String token,
                                            @RequestBody Question question) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing or invalid token");
        }

        String username = jwtUtil.extractUsername(token.substring(7));

        User admin = userRepository.findByUsername(username);
        if (admin == null || !admin.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Not an Admin");
        }

        // Ensure the admin_id is set
        question.setCreatedBy(admin);

        Question createdQuestion = questionService.createQuestion(question);

        System.out.println("Controller QUESTIONS: " + question);

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
    @GetMapping("/my-questions")
    public ResponseEntity<List<Question>> getMyQuestions(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User admin = userRepository.findByUsername(username);
        List<Question> questions = questionService.getQuestionsByAdmin(admin.getId());
        return ResponseEntity.ok(questions);
    }


    // Delete a question by ID
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long questionId) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User admin = userRepository.findByUsername(username);

        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        boolean deleted = questionService.deleteQuestion(questionId, admin.getId());
        if (deleted) {
            return ResponseEntity.ok("Question deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own questions.");
        }
    }
}
