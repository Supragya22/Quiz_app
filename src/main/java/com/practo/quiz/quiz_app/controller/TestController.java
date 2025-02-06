package com.practo.quiz.quiz_app.controller;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    // Endpoint to create a new test
    @PostMapping
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        Test createdTest = testService.createTest(test);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }

    // Endpoint to get all tests
    @GetMapping
    public ResponseEntity<List<Test>> getAllTests() {
        List<Test> tests = testService.getAllTests();
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
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        boolean isDeleted = testService.deleteTest(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
