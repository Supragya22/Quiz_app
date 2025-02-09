package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Answer;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.service.TestTakerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @PutMapping("/{userId}/assign-test/{testId}")
//    public ResponseEntity<String> assignTest(@PathVariable Long userId, @PathVariable Long testId) {
//        String response = testTakerService.assignTest(userId, testId);
//        return ResponseEntity.ok(response);
//    }

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
        return ResponseEntity.ok(testTakerService.startTest(userId, testId));
    }

    @PostMapping("/{userId}/submit-test/{testId}")
    public ResponseEntity<String> submitTest(@PathVariable Long userId, @PathVariable Long testId, @RequestBody List<Answer> answers) {
        String result = testTakerService.submitTest(userId, testId, answers);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/test-result/{testId}")
    public ResponseEntity<Map<String, Object>> viewTestResult(@PathVariable Long userId, @PathVariable Long testId) {
        return ResponseEntity.ok(testTakerService.getTestResult(userId, testId));
    }
}
