package com.upgrad.quora.api.exception;

import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ AuthorizationFailedException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(AuthorizationFailedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(UserNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ InvalidQuestionException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(InvalidQuestionException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ AnswerNotFoundException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(AnswerNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ SignOutRestrictedException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(SignOutRestrictedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ SignUpRestrictedException.class })
    protected ResponseEntity<Object> handleAuthenticationFailure(SignUpRestrictedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put( "code", ex.getCode() );
        body.put( "message", ex.getErrorMessage() );
        return new ResponseEntity( body, HttpStatus.CONFLICT);
    }
}
