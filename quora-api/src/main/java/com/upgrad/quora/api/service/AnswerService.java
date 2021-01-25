package com.upgrad.quora.api.service;

import com.upgrad.quora.api.requestresponse.AnswerEditRequest;
import com.upgrad.quora.api.requestresponse.AnswerRequest;
import com.upgrad.quora.service.dao.AnswerRepository;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Answer createAnswer(String questionId, AnswerRequest answerRequest, Users loggedInUser) throws InvalidQuestionException {

        Question question = questionRepository.findByUuid( questionId );

        if (question == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        Answer answer = new Answer();
        answer.setQuestionId( question );
        answer.setUserId( loggedInUser );
        answer.setAns( answerRequest.getAnswer() );
        answer.setUuid( UUID.randomUUID().toString() );
        answer.setDate( LocalDateTime.now() );

        return answerRepository.save( answer );
    }

    public Answer editAnswer(String answerId, AnswerEditRequest editRequest, Users loggedInUser) throws AnswerNotFoundException, AuthorizationFailedException {

        Answer answer = answerRepository.findByUuid( answerId );

        if (answer == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        if (answer.getUserId()!= loggedInUser) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

        answer.setAns( editRequest.getContent() );

        return answerRepository.save( answer );
    }

    public void deleteAnswer(String answerId, Users loggedInUser) throws AnswerNotFoundException, AuthorizationFailedException {

        Answer answer = answerRepository.findByUuid( answerId );

        if (answer == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        if (answer.getUserId() != loggedInUser || answer.getUserId().getRole().equals( "nonadmin" )) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

        answerRepository.delete( answer );
    }

    public Iterable<Answer> getAllAnswersToQuestion(String questionId) throws InvalidQuestionException {

        Question question = questionRepository.findByUuid( questionId );

        if (question == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }

        return answerRepository.findByQuestionId( question );
    }
}
