package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.TestTaker;
import com.practo.quiz.quiz_app.model.User;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.repository.TestTakerRepository;
import com.practo.quiz.quiz_app.repository.TestRepository;
import com.practo.quiz.quiz_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTakerService {

    @Autowired
    private TestTakerRepository testTakerRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    // Start a test for a test taker
    public TestTaker startTest(Long userId, Long testId) {
        Test test = testRepository.findById(testId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (test != null && user != null) {
            TestTaker testTaker = new TestTaker();
            testTaker.setUser(user);
            testTaker.setTest(test);
            testTaker.setAnswers(List.of()); // Initialize with empty answers
            return testTakerRepository.save(testTaker);
        }
        return null;
    }

    // Submit the test
    public TestTaker submitTest(Long userId, Long testId) {
        TestTaker testTaker = testTakerRepository.findByUserIdAndTestId(userId, testId);

        if (testTaker != null && !testTaker.isSubmitted()) {
            testTaker.setSubmitted(true);
            return testTakerRepository.save(testTaker);
        }
        return null;
    }

    // Update an answer for a specific question
    public TestTaker updateAnswer(Long userId, Long testId, String answer) {
        TestTaker testTaker = testTakerRepository.findByUserIdAndTestId(userId, testId);
        
        if (testTaker != null && !testTaker.isSubmitted()) {
            List<String> answers = testTaker.getAnswers();
            answers.add(answer); // Assuming answers are stored in the correct order
            testTaker.setAnswers(answers);
            return testTakerRepository.save(testTaker);
        }
        return null;
    }

    // Calculate the score for a test taker
    public int calculateScore(Long userId, Long testId) {
        TestTaker testTaker = testTakerRepository.findByUserIdAndTestId(userId, testId);

        if (testTaker != null && testTaker.isSubmitted()) {
            List<String> answers = testTaker.getAnswers();
            int score = 0;

            // Calculate score based on answers (assuming we compare answers with correct ones)
            Test test = testTaker.getTest();
            for (int i = 0; i < answers.size(); i++) {
                if (test.getQuestions().get(i).getCorrectAnswer().equals(answers.get(i))) {
                    score++;
                }
            }
            return score;
        }
        return 0;
    }
}
