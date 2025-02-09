package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.repository.TestRepository;
import com.practo.quiz.quiz_app.repository.QuestionRepository;
import com.practo.quiz.quiz_app.repository.TestTakerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestTakerRepository testTakerRepository;

    // Create a new test
    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    // Get all tests created by admin
    public List<Test> getTestsByAdmin(Long adminId) {
        return testRepository.findByAdminId(adminId);
    }


    // Get a test by ID
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

    // Assign questions to a test
    public boolean assignQuestionsToTest(Long testId, List<Long> questionIds, Long adminId) {
        Test test = testRepository.findById(testId).orElse(null);;
        if (test == null || !test.getCreatedBy().getId().equals(adminId)) {
            return false;  // Either test not found or user is not authorized to assign questions
        }

        // Fetch only the questions that belong to the admin
        List<Question> questions = questionRepository.findByIdsAndAdminId(questionIds, adminId);

        if (questions.size() != questionIds.size()) {
            return false;  // Some questions do not belong to the admin
        }

        // Add questions to the test ensuring no duplicates
        for (Question question : questions) {
            // Check if the question is already assigned to this test
            boolean alreadyAssigned = test.getQuestions().stream()
                    .anyMatch(q -> q.getId().equals(question.getId()));

            if (!alreadyAssigned) {
                test.getQuestions().add(question);  // Add question if it's not already assigned
            }
        }
        testRepository.save(test);

        return true;
    }

    // Method to get questions for a specific test
    public List<Question> getQuestionsByTestId(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));
        return questionRepository.findByTestId(testId);// Fetch questions from the test
    }

    // Update a test
    public Test updateTest(Long id, Test updatedTest) {
        Optional<Test> existingTest = testRepository.findById(id);
        if (existingTest.isPresent()) {
            Test test = existingTest.get();
            test.setName(updatedTest.getName());
            test.setDescription(updatedTest.getDescription());
            test.setStartTime(updatedTest.getStartTime());
            test.setEndTime(updatedTest.getEndTime());
            test.setActive(updatedTest.isActive());
            return testRepository.save(test);
        }
        return null; // Return null if the test doesn't exist
    }

    @Transactional
    public void deleteTest(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));

        // Remove the test from associated questions
        for (Question question : test.getQuestions()) {
            question.getTests().remove(test); // Unlink test from question
            questionRepository.save(question); // Save updated question
        }

        testRepository.delete(test);
    }
//    public void assignTestToTestTakers(Long testId, List<Long> testTakerIds) {
//        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
//
//        for (Long testTakerId : testTakerIds) {
//            TestTaker testTaker = testTakerRepository.findById(testTakerId)
//                    .orElseThrow(() -> new RuntimeException("TestTaker not found"));
//            testTaker.getAssignedTests().add(test);
//            testTakerRepository.save(testTaker);
//        }
//    }

    // Fetch all test-takers assigned to a test
//    public List<TestTaker> getAssignedTestTakers(Long testId) {
//        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
//        return testTakerRepository.findByAssignedTests(test);
//    }

}
