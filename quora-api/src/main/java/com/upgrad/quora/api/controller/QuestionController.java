package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.requestresponse.*;
import com.upgrad.quora.api.service.QuestionService;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @PostMapping("/question/create")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionRequest questionRequest, @RequestHeader("Authorization") String token) throws AuthorizationFailedException {

        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to post a question" );

        QuestionResponse response = questionService.createQuestion( questionRequest, loggedInUser );

        return new ResponseEntity( response, HttpStatus.CREATED );
    }

    @GetMapping("/question/all")
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("Authorization") String token) throws AuthorizationFailedException {

        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to get all questions" );

        List<QuestionDetailsResponse> response = new ArrayList<QuestionDetailsResponse>();
        questionService.getAllQuestions().forEach( question -> {
            QuestionDetailsResponse details = new QuestionDetailsResponse(
                    question.getUuid(),
                    question.getContent()
            );
            response.add(details);} );

        return new ResponseEntity( response, HttpStatus.OK );
    }

    @PutMapping("/question/edit/{questionId}")
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable String questionId, @RequestBody QuestionEditRequest questionEditRequest, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, InvalidQuestionException {

        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to edit the question" );

        Question updatedQuestion = questionService.updateQuestion( questionId, questionEditRequest, loggedInUser );
        QuestionEditResponse response = new QuestionEditResponse( updatedQuestion.getUuid(), "QUESTION EDITED" );

        return new ResponseEntity( response, HttpStatus.OK );
    }

    @DeleteMapping("/question/delete/{questionId}")
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable String questionId, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, InvalidQuestionException {
        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to delete a question" );

        questionService.deleteQuestion( questionId, loggedInUser );


        return new ResponseEntity( new QuestionDeleteResponse( questionId, "QUESTION DELETED" ), HttpStatus.OK );
    }

    @GetMapping("question/all/{userId}")
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable String userId, @RequestHeader("Authorization") String token) throws AuthorizationFailedException, UserNotFoundException {
        Users loggedInUser = getLoggedUser( token, "User is signed out.Sign in first to get all questions posted by a specific user" );

        List<QuestionDetailsResponse> questions = new ArrayList<QuestionDetailsResponse>();

        questionService.getQuestionsForUser( userId ).forEach( question -> {
            QuestionDetailsResponse details = new QuestionDetailsResponse( question.getUuid(), question.getContent() );
            questions.add(details);
        } );

        return new ResponseEntity( questions, HttpStatus.OK );
    }

    private Users getLoggedUser(String credentials, String errorMessage) throws AuthorizationFailedException {

        UserAuth auth = userAuthRepository.findByAccessToken( credentials );

        // Check if the token exists. If not, throw exception
        if (auth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Throw exception if the user has logged out
        if (auth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", errorMessage);
        }

        Users loggedInUser = auth.getUserId();

        return loggedInUser;
    }

}
