package com.practo.quiz.quiz_app.service;

import com.practo.quiz.quiz_app.model.Question;
import com.practo.quiz.quiz_app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Create a new question
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Get all questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
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
            question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            return questionRepository.save(question);
        }
        return null; // Return null if the question doesn't exist
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
