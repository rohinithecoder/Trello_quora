package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.requestresponse.*;
import com.upgrad.quora.api.service.AnswerService;
import com.upgrad.quora.service.dao.AnswerRepository;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AnswerController {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository QuestionRepository;

    @Autowired
    AnswerService answerService;

    @Autowired
    private UserRepository UserRepository;

    @PostMapping("/question/{questionId}/answer/create")
    public ResponseEntity<AnswerResponse> createAnswer(@RequestBody AnswerRequest answerRequest, @PathVariable String questionId, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, InvalidQuestionException {

        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to post an answer" );

        Answer newAnswer = answerService.createAnswer(questionId, answerRequest, loggedInUser);

        return new ResponseEntity( new AnswerResponse( newAnswer.getUuid(), "ANSWER CREATED" ), HttpStatus.CREATED );
    }

    @PutMapping("/answer/edit/{answerId}")
    public ResponseEntity editAnswerContent(@PathVariable String answerId, @RequestBody AnswerEditRequest editRequest, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, AnswerNotFoundException {

        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to edit an answer" );

        Answer updatedAnswer = answerService.editAnswer( answerId, editRequest, loggedInUser );

        return new ResponseEntity( new AnswerEditResponse( updatedAnswer.getUuid(), "ANSWER EDITED" ), HttpStatus.OK );

    }

    @DeleteMapping("/answer/delete/{answerId}")
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable String answerId, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, AnswerNotFoundException {
        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to delete an answer" );

        answerService.deleteAnswer( answerId, loggedInUser );

        return new ResponseEntity( new AnswerDeleteResponse( answerId, "ANSWER DELETED" ), HttpStatus.OK );

    }

    @GetMapping("answer/all/{questionId}")
    public ResponseEntity getAllAnswersToQuestion(@PathVariable String questionId, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, InvalidQuestionException {
        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to get the answers" );

        List<AnswerDetailsResponse> answers = new ArrayList<AnswerDetailsResponse>();
                answerService.getAllAnswersToQuestion( questionId ).forEach( answer -> {
            AnswerDetailsResponse response = new AnswerDetailsResponse( answer.getUuid(), answer.getQuestionId().getContent(), answer.getAns() );
            answers.add( response );

        } );

        return new ResponseEntity( answers, HttpStatus.OK );
    }

    private Users getLoggedUser(String credentials, String errorMessage) throws AuthorizationFailedException {

        UserAuth auth = userAuthRepository.findByAccessToken( credentials );

        if (auth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (auth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", errorMessage);
        }

        Users loggedInUser = auth.getUserId();

        return loggedInUser;
    }

}
