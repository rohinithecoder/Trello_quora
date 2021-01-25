package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.requestresponse.UserDeleteResponse;
import com.upgrad.quora.api.service.AdminService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @DeleteMapping("/admin/user/{userId}")
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable String userId, @RequestHeader("Authorization") String credentials) throws AuthorizationFailedException, UserNotFoundException {

        UserDeleteResponse response = adminService.userDelete( userId, credentials );

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
