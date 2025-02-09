//package com.practo.quiz.quiz_app.controller;
//
//import com.practo.quiz.quiz_app.model.Test;
//import com.practo.quiz.quiz_app.model.TestTaker;
//import com.practo.quiz.quiz_app.model.Answer;
//import com.practo.quiz.quiz_app.model.User;
//import com.practo.quiz.quiz_app.service.TestTakerService;
//import com.practo.quiz.quiz_app.service.TestService;
//import com.practo.quiz.quiz_app.security.JwtUtil;
//import com.practo.quiz.quiz_app.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/test-takers")
//public class TestTakerController {
//
//    @Autowired
//    private TestTakerService testTakerService;
//
//    @Autowired
//    private TestService testService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    // Endpoint to get all the tests assigned to the test-taker
//    @GetMapping("/assigned-tests")
//    public ResponseEntity<?> getAssignedTests(@RequestHeader("Authorization") String token) {
//        // Extract user from JWT token
//        String username = jwtUtil.extractUsername(token.substring(7));
//        User user = userRepository.findByUsername(username);
//
//        // Get the assigned tests for the test-taker
//        List<Test> assignedTests = testTakerService.getAssignedTestsForTestTaker(user.getId());
//
//        // Return the list of assigned tests or a message if no tests found
//        if (assignedTests.isEmpty()) {
//            return ResponseEntity.status(404).body("No tests assigned to this user.");
//        }
//        return ResponseEntity.ok(assignedTests);
//    }
//
//    // Endpoint to start a test (only if it's active)
//    @PostMapping("/{testId}/start")
//    public ResponseEntity<?> startTest(@RequestHeader("Authorization") String token, @PathVariable Long testId) {
//        // Extract user from JWT token
//        String username = jwtUtil.extractUsername(token.substring(7));
//        User user = userRepository.findByUsername(username);
//
//        // Ensure the test is assigned to this user
//        if (!testTakerService.isTestAssignedToUser(testId, user.getId())) {
//            return ResponseEntity.status(403).body("Test not assigned to the user.");
//        }
//
//        // Check if the test is active
//        Test test = testService.getTestById(testId);
//        if (!test.isActive()) {
//            return ResponseEntity.status(403).body("Test is not active.");
//        }
//
//        // Mark test as started for the user (create or update TestTaker record)
//        testTakerService.startTestForUser(testId, user.getId());
//
//        return ResponseEntity.ok("Test started successfully.");
//    }
//
//    // Endpoint to submit the test
//    @PostMapping("/{testId}/submit")
//    public ResponseEntity<?> submitTest(@RequestHeader("Authorization") String token, @PathVariable Long testId,
//                                        @RequestBody List<Answer> answers) {
//        // Extract user from JWT token
//        String username = jwtUtil.extractUsername(token.substring(7));
//        User user = userRepository.findByUsername(username);
//
//        // Ensure the test is assigned to this user
//        if (!testTakerService.isTestAssignedToUser(testId, user.getId())) {
//            return ResponseEntity.status(403).body("Test not assigned to the user.");
//        }
//
//        // Submit the answers and calculate the score
//        int score = testTakerService.submitTest(testId, user.getId(), answers);
//
//        return ResponseEntity.ok("Test submitted successfully. Your score is: " + score);
//    }
//
//    // Endpoint to view the scorecard (score for the test)
//    @GetMapping("/{testId}/score")
//    public ResponseEntity<?> getScore(@RequestHeader("Authorization") String token, @PathVariable Long testId) {
//        // Extract user from JWT token
//        String username = jwtUtil.extractUsername(token.substring(7));
//        User user = userRepository.findByUsername(username);
//
//        // Get the score for this user for the specific test
//        Integer score = testTakerService.getTestScoreForUser(testId, user.getId());
//
//        if (score == null) {
//            return ResponseEntity.status(404).body("Test score not found.");
//        }
//
//        return ResponseEntity.ok("Your score for the test is: " + score);
//    }
//}
