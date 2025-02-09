package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.*;
import com.practo.quiz.quiz_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestTakerService {

    @Autowired
    private TestTakerRepository testTakerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    /**
     * Assigns a test to a user.
     */
    public String assignTest(Long userId, Long testId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (testTakerRepository.findByUserIdAndTestId(userId, testId).isPresent()) {
            return "Test already assigned to the user.";
        }

        TestTaker testTaker = new TestTaker(user, test);
        testTakerRepository.save(testTaker);
        return "Test assigned successfully.";
    }

    /**
     * Retrieves all assigned tests for a user.
     */
    public List<Test> getAssignedTests(Long userId) {
        return testTakerRepository.findAssignedTestsByUserId(userId);
    }

    /**
     * Retrieves the ongoing test for a user.
     */
    public Optional<TestTaker> getOngoingTest(Long userId) {
        return testTakerRepository.findOngoingTestByUserId(userId);
    }

    /**
     * Starts a test by retrieving all questions for the assigned test.
     */
    public List<Question> startTest(Long userId, Long testId) {
        return testTakerRepository.findByUserIdAndTestId(userId, testId)
                .map(testTaker -> questionRepository.findByTestId(testId))
                .orElseThrow(() -> new RuntimeException("Test not assigned to the user."));
    }

    /**
     * Submits a test and calculates the score.
     */
    public String submitTest(Long userId, Long testId, List<Answer> answers) {
        System.out.println("TEST TAKER SERVICE: SUBMIT TEST: " + userId + " " + testId + " " + answers);

        TestTaker testTaker = testTakerRepository.findByUserIdAndTestId(userId, testId)
                .orElseThrow(() -> new RuntimeException("Test not found for this user."));

        if (testTaker.isSubmitted()) {
            return "Test already submitted.";
        }

        // Fetch full Question objects and correct answers
        Map<Long, Integer> correctAnswers = questionRepository.findByTestId(testId)
                .stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectOptionIndex));

        System.out.println("TEST TAKER: CORRECT ANSWERS: " + correctAnswers);

        for (Answer answer : answers) {
            if (answer.getQuestion() == null || answer.getQuestion().getId() == null) {
                throw new RuntimeException("Invalid question reference in the answer.");
            }

            // Fetch the full Question object
            Question question = questionRepository.findById(answer.getQuestion().getId())
                    .orElseThrow(() -> new RuntimeException("Question not found."));

            // Validate selected option
            boolean isCorrect = correctAnswers.getOrDefault(question.getId(), -1) == answer.getSelectedOption();

            // Ensure Answer object is fully populated
            answer.setUser(testTaker.getUser());
            answer.setTest(testTaker.getTest());
            answer.setTestTaker(testTaker);
            answer.setQuestion(question);
            answer.setCorrect(isCorrect);
        }

        answerRepository.saveAll(answers);

        // Calculate and save the score
        int score = calculateScore(testId, answers);
        testTaker.setScore(score);
        testTaker.setSubmitted(true);
        testTakerRepository.save(testTaker);

        return "Test submitted successfully. Your score: " + score;
    }

    /**
     * Calculates the total score for the test.
     */
    private int calculateScore(Long testId, List<Answer> answers) {
        Map<Long, Integer> correctAnswers = questionRepository.findByTestId(testId)
                .stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectOptionIndex));

        return (int) answers.stream()
                .filter(answer -> {
                    Integer correctOption = correctAnswers.get(answer.getQuestion().getId());
                    return correctOption != null && correctOption == answer.getSelectedOption();
                })
                .count();
    }

    /**
     * Retrieves the test result for a user.
     */
    public Map<String, Object> getTestResult(Long userId, Long testId) {
        TestTaker testTaker = testTakerRepository.findByUserIdAndTestId(userId, testId)
                .orElseThrow(() -> new RuntimeException("Test result not found."));

        return Map.of(
                "testId", testId,
                "userId", userId,
                "score", testTaker.getScore(),
                "submitted", testTaker.isSubmitted()
        );
    }
}
