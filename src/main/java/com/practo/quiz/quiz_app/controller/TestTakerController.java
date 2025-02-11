package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.dto.TestTakerDTO;
import com.practo.quiz.quiz_app.model.Answer;
import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.service.TestTakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test-takers")
public class TestTakerController {

    private final TestTakerService testTakerService;

    public TestTakerController(TestTakerService testTakerService) {
        this.testTakerService = testTakerService;
    }

    @GetMapping("/{userId}/tests")
    public ResponseEntity<List<Test>> getAssignedTests(@PathVariable Long userId) {
        return ResponseEntity.ok(testTakerService.getAssignedTests(userId));
    }

    @GetMapping("/{userId}/ongoing-test")
    public ResponseEntity<Optional<Test>> getOngoingTest(@PathVariable Long userId) {
        return ResponseEntity.ok(testTakerService.getOngoingTest(userId).map(TestTaker::getTest));
    }

    @GetMapping("/{userId}/start-test/{testId}")
    public ResponseEntity<?> startTest(@PathVariable Long userId, @PathVariable Long testId) {
      //  return ResponseEntity.ok(testTakerService.startTest(userId, testId));
            try {
                List<Question> questions = testTakerService.startTest(userId, testId);
                return ResponseEntity.ok(questions);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", e.getMessage()));
            }
    }

    @PostMapping("/{userId}/submit-test/{testId}")
    public ResponseEntity<?> submitTest(@PathVariable Long userId, @PathVariable Long testId, @RequestBody List<Answer> answers) {
//        TestTakerDTO result = testTakerService.submitTest(userId, testId, answers);
//        return ResponseEntity.ok(result);
        try {
            TestTakerDTO result = testTakerService.submitTest(userId, testId, answers);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/test-result/{testId}")
    public ResponseEntity<TestTakerDTO> viewTestResult(@PathVariable Long userId, @PathVariable Long testId) {
        return ResponseEntity.ok(testTakerService.getTestResult(userId, testId));
    }
}
