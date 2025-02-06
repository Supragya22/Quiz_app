package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.service.TestTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testTakers")
public class TestTakerController {

    @Autowired
    private TestTakerService testTakerService;

    //Start a test for a user
    @PostMapping("/start/{userId}/{testId}")
    public ResponseEntity<TestTaker> startTestForUser(@PathVariable Long userId, @PathVariable Long testId) {
        TestTaker testTaker = testTakerService.startTest(userId, testId);
        if (testTaker != null) {
            return new ResponseEntity<>(testTaker, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Submit a test for a user
    @PatchMapping("/submit/{userId}/{testId}")
    public ResponseEntity<TestTaker> submitTestForUser(@PathVariable Long userId, @PathVariable Long testId) {
        TestTaker testTaker = testTakerService.submitTest(userId, testId);
        if (testTaker != null) {
            return ResponseEntity.ok(testTaker);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Test not found or already submitted
    }

    //Update an answer for a specific test
    @PatchMapping("/updateAnswer/{userId}/{testId}")
    public ResponseEntity<TestTaker> updateUserAnswer(
            @PathVariable Long userId,
            @PathVariable Long testId,
            @RequestParam String answer) {
        TestTaker testTaker = testTakerService.updateAnswer(userId, testId, answer);
        if (testTaker != null) {
            return ResponseEntity.ok(testTaker);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Test not found or already submitted
    }

    //Calculate the score for a user
    @GetMapping("/score/{userId}/{testId}")
    public ResponseEntity<Integer> calculateUserScore(@PathVariable Long userId, @PathVariable Long testId) {
        int score = testTakerService.calculateScore(userId, testId);
        return ResponseEntity.ok(score);
    }
}
