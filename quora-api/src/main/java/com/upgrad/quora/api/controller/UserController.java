package com.upgrad.quora.api.controller;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.upgrad.quora.api.requestresponse.SigninResponse;
import com.upgrad.quora.api.requestresponse.SignoutResponse;
import com.upgrad.quora.api.requestresponse.SignupUserRequest;
import com.upgrad.quora.api.requestresponse.SignupUserResponse;
import com.upgrad.quora.api.service.UserService;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @PostMapping("/user/signup")
    public ResponseEntity<SignupUserResponse> signup(@RequestBody SignupUserRequest signUpRequest) throws SignUpRestrictedException {
        Users newUser = userService.signUpUser( signUpRequest );

        return new ResponseEntity(
                new SignupUserResponse( newUser.getUuid(), "USER SUCCESSFULLY REGISTERED" ), HttpStatus.CREATED );
    }

    @PostMapping("/user/signin")
    public  ResponseEntity<SigninResponse> signin(@RequestHeader("Authorization") String credentials) throws Base64DecodingException, AuthenticationFailedException {
        Map<String, Object> response = userService.signInUser( credentials );

        HttpHeaders headers = new HttpHeaders();
        headers.set("access_token", response.get( "access_token" ).toString());

        return new ResponseEntity( response.get("signInResponse"), headers, HttpStatus.OK );
    }

    @PostMapping("/user/signout")
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("Authorization") String token) throws SignOutRestrictedException {

        UserAuth auth = userService.signOutUser( token );

        return new ResponseEntity( new SignoutResponse( auth.getUserId().getUuid(), "SIGNED OUT SUCCESSFULLY" ), HttpStatus.OK );
    }

}