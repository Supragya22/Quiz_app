package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.service.TestTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestTakerService testTakerService;

    // Start the test for a test taker (invited user)
    @PostMapping("/{testId}/start")
    public ResponseEntity<TestTaker> startTest(@PathVariable Long testId, @RequestParam Long userId) {
        TestTaker testTaker = testTakerService.startTest(userId, testId);
        return testTaker != null ? ResponseEntity.ok(testTaker) : ResponseEntity.notFound().build();
    }

    // Submit the test
    @PostMapping("/{testId}/submit")
    public ResponseEntity<TestTaker> submitTest(@PathVariable Long testId, @RequestParam Long userId) {
        TestTaker testTaker = testTakerService.submitTest(userId, testId);
        return testTaker != null ? ResponseEntity.ok(testTaker) : ResponseEntity.notFound().build();
    }

    // Update an answer for a specific question
    @PostMapping("/{testId}/answers")
    public ResponseEntity<TestTaker> updateAnswer(@PathVariable Long testId, @RequestParam Long userId, @RequestParam String answer) {
        TestTaker testTaker = testTakerService.updateAnswer(userId, testId, answer);
        return testTaker != null ? ResponseEntity.ok(testTaker) : ResponseEntity.notFound().build();
    }

    // Get the score of the test taker
    @GetMapping("/{testId}/score")
    public ResponseEntity<Integer> getScore(@PathVariable Long testId, @RequestParam Long userId) {
        int score = testTakerService.calculateScore(userId, testId);
        return ResponseEntity.ok(score);
    }
}
