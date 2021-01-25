package com.upgrad.quora.api.service;

import com.upgrad.quora.api.requestresponse.QuestionEditRequest;
import com.upgrad.quora.api.requestresponse.QuestionRequest;
import com.upgrad.quora.api.requestresponse.QuestionResponse;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    public QuestionResponse createQuestion(QuestionRequest questionRequest, Users loggerInUser) {
        Question newQuestion = new Question();

        newQuestion.setUuid( UUID.randomUUID().toString() );
        newQuestion.setContent( questionRequest.getContent() );
        newQuestion.setUserId( loggerInUser );
        newQuestion.setDate( new Date() );

        Question savedQuestion = questionRepository.save( newQuestion );
        return new QuestionResponse( savedQuestion.getUuid(), "QUESTION CREATED" );
    }

    public Iterable<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question updateQuestion(String uuid, QuestionEditRequest questionEditRequest, Users loggedInUser) throws AuthorizationFailedException, InvalidQuestionException {
        Question question = questionRepository.findByUuid( uuid );

        if (question == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (question.getUserId() != loggedInUser) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        question.setContent( questionEditRequest.getContent() );
        Question updatedQuestion = questionRepository.save( question );

        return updatedQuestion;
    }

    public void deleteQuestion(String uuid, Users loggedInUser) throws InvalidQuestionException, AuthorizationFailedException {
        Question question = questionRepository.findByUuid( uuid );

        if (question == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (question.getUserId() != loggedInUser || loggedInUser.getRole().equals( "nonadmin" )) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

        questionRepository.delete( question );
    }

    public Iterable<Question> getQuestionsForUser(String userId) throws UserNotFoundException {
        Users user = userRepository.findByUuid( userId );
        if (user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }

        return questionRepository.findByUserId( user );
    }
}
