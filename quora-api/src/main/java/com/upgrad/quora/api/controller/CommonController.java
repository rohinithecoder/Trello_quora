package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.service.CommonService;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping("/userprofile/{userId}")
    public ResponseEntity<Users> userProfile(@PathVariable String userId, @RequestHeader("Authorization") String credentials) throws AuthorizationFailedException, UserNotFoundException {

        Users user = commonService.getUserProfile(userId, credentials);

        return new ResponseEntity( user, HttpStatus.OK );
    }
}
