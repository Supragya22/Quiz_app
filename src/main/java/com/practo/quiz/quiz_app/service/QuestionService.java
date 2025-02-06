package com.practo.quiz.quiz_app.service;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.repository.QuestionRepository;
import com.practo.quiz.quiz_app.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    // Create a new question and associate it with a test
    public Question createQuestion(Long testId, Question question) {
        Optional<Test> test = testRepository.findById(testId);
        if (test.isPresent()) {
            // Associate the question with the test (adding it to the test's question list)
            questionRepository.save(question);
            return question;
        }
        return null; // Return null if the test doesn't exist
    }

    // Get all questions for a specific test
    public List<Question> getQuestionsByTestId(Long testId) {
        Optional<Test> test = testRepository.findById(testId);
        if (test.isPresent()) {
            return test.get().getQuestions();
        }
        return null; // Return null if the test doesn't exist
    }

    // Get all questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Delete a question by ID
    public boolean deleteQuestion(Long questionId) {
        if (questionRepository.existsById(questionId)) {
            questionRepository.deleteById(questionId);
            return true;
        }
        return false;
    }
}
