package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    // Create a new test
    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    // Get all tests
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    // Get a test by ID
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

    // Update a test by ID
    public Test updateTest(Long id, Test updatedTest) {
        // Fetch the test from the database
        Optional<Test> existingTest = testRepository.findById(id);
        if (existingTest.isPresent()) {
            Test test = existingTest.get();
            test.setName(updatedTest.getName());
            test.setDescription(updatedTest.getDescription());
            test.setStartTime(updatedTest.getStartTime());
            test.setEndTime(updatedTest.getEndTime());
            test.setQuestions(updatedTest.getQuestions());
            test.setActive(updatedTest.isActive());
            return testRepository.save(test);
        }
        return null; // Return null if test does not exist
    }

    // Delete a test by ID
    public boolean deleteTest(Long id) {
        if (testRepository.existsById(id)) {
            testRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
