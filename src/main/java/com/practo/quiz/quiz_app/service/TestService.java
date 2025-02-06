package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.repository.TestRepository;
import com.practo.quiz.quiz_app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

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

    // Assign questions to a test
    public Test assignQuestionsToTest(Long testId, List<Long> questionIds) {
        Optional<Test> testOptional = testRepository.findById(testId);
        if (testOptional.isPresent()) {
            Test test = testOptional.get();
            List<Question> questions = questionRepository.findAllById(questionIds);
            test.setQuestions(questions);
            return testRepository.save(test);
        }
        return null; // Return null if the test doesn't exist
    }
    // Method to get questions for a specific test
    public List<Question> getQuestionsByTestId(Long testId) {
        Optional<Test> test = testRepository.findById(testId);
        return test.map(Test::getQuestions).orElse(null);  // Return questions if test exists
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

    // Delete a test
    public boolean deleteTest(Long id) {
        if (testRepository.existsById(id)) {
            testRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
