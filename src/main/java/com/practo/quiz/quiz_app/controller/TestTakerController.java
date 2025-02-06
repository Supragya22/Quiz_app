package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.service.TestTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testTakers")
public class TestTakerController {

    @Autowired
    private TestTakerService testTakerService;

    // Endpoint to start a test for a user
    @PostMapping("/startTest")
    public TestTaker startTest(@RequestParam Long userId, @RequestParam Long testId) {
        return testTakerService.startTest(userId, testId);
    }

    // Endpoint to submit the test for a user
    @PatchMapping("/submitTest")
    public TestTaker submitTest(@RequestParam Long userId, @RequestParam Long testId) {
        return testTakerService.submitTest(userId, testId);
    }

    // Endpoint to update an answer for a specific test
    @PatchMapping("/updateAnswer")
    public TestTaker updateAnswer(@RequestParam Long userId, @RequestParam Long testId, @RequestParam String answer) {
        return testTakerService.updateAnswer(userId, testId, answer);
    }

    // Endpoint to calculate the score for a test taker
    @GetMapping("/calculateScore")
    public int calculateScore(@RequestParam Long userId, @RequestParam Long testId) {
        return testTakerService.calculateScore(userId, testId);
    }
}
