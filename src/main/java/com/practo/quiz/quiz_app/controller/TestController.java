package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.*;
import com.practo.quiz.quiz_app.repository.UserRepository;
import com.practo.quiz.quiz_app.security.JwtUtil;
import com.practo.quiz.quiz_app.service.TestService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint to create a new test
    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody Test test, @RequestHeader("Authorization") String token) {
        try {
            // Ensure the token is formatted correctly
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Invalid Token Format", HttpStatus.UNAUTHORIZED);
            }

            // Extract username from token
            String username = jwtUtil.extractUsername(token.substring(7));

            // Fetch admin user
            User admin = userRepository.findByUsername(username);
            if (admin == null || !admin.getRole().equals("ROLE_ADMIN")) {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }

            // Set the admin as the creator
            test.setCreatedBy(admin);

            // Save test
            Test createdTest = testService.createTest(test);
            return new ResponseEntity<>(createdTest, HttpStatus.CREATED);

        } catch (MalformedJwtException e) {
            return new ResponseEntity<>("Malformed JWT Token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{testId}/add-questions")
    public ResponseEntity<String> addQuestionsToTest(
            @PathVariable Long testId,
            @RequestBody Map<String, List<Long>> body,
            @RequestHeader("Authorization") String token)
    {
        // Ensure token is formatted correctly
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Token Format", HttpStatus.UNAUTHORIZED);
        }

        // Extract username from token
        String username = jwtUtil.extractUsername(token.substring(7));

        List<Long> questionIds = body.get("questionIds");
        // Fetch admin user
        User admin = userRepository.findByUsername(username);
        if (admin == null || !admin.getRole().equals("ROLE_ADMIN")) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }

        // Assign questions ensuring they belong to the admin
        boolean assigned = testService.assignQuestionsToTest(testId, questionIds, admin.getId());
        if (assigned) {
            return ResponseEntity.ok("Questions assigned to test successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only assign questions you created.");
        }
    }


    // Endpoint to get all tests
    @GetMapping("/my-tests")
    public ResponseEntity<List<Test>> getMyTests(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User admin = userRepository.findByUsername(username);
        List<Test> tests = testService.getTestsByAdmin(admin.getId());
        return ResponseEntity.ok(tests);
    }


    // Endpoint to get a test by ID
    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        Optional<Test> test = testService.getTestById(id);
        return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //New Endpoint: Get questions for a specific test
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<Question>> getQuestionsByTestId(@PathVariable Long id) {
        List<Question> questions = testService.getQuestionsByTestId(id);
        if (questions != null && !questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.notFound().build();  // No questions found
    }

    // Endpoint to update a test by ID
    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Long id, @RequestBody Test updatedTest) {
        Test test = testService.updateTest(id, updatedTest);
        if (test != null) {
            return ResponseEntity.ok(test);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a test by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return ResponseEntity.ok("Test deleted successfully, but questions are retained.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the test.");
        }
    }

}
