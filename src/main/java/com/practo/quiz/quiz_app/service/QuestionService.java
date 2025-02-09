package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.model.Test;
import com.practo.quiz.quiz_app.repository.QuestionRepository;
import com.practo.quiz.quiz_app.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    // Create a new question
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Get all questions created by admin
    public List<Question> getQuestionsByAdmin(Long adminId) {
        return questionRepository.findByCreatedById(adminId);
    }


    // Get a specific question by ID
    public Optional<Question> getQuestionById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    // Update a question
    public Question updateQuestion(Long questionId, Question updatedQuestion) {
        Optional<Question> existingQuestion = questionRepository.findById(questionId);
        if (existingQuestion.isPresent()) {
            Question question = existingQuestion.get();
            question.setQuestionText(updatedQuestion.getQuestionText());
            question.setOption1(updatedQuestion.getOption1());
            question.setOption2(updatedQuestion.getOption2());
            question.setOption3(updatedQuestion.getOption3());
            question.setOption4(updatedQuestion.getOption4());
            question.setCorrectOptionIndex(updatedQuestion.getCorrectOptionIndex());
            return questionRepository.save(question);
        }
        return null; // Return null if the question doesn't exist
    }

    // Delete a question by ID
    public boolean deleteQuestion(Long questionId, Long id) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();

            // Remove the question from all associated tests
            for (Test test : question.getTests()) {
                test.getQuestions().remove(question);
                testRepository.save(test); // Save each test after modification
            }
            // Now delete the question
            questionRepository.delete(question);
            return true;
        }
        return false;
    }
}
