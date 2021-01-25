package com.upgrad.quora.api.service;

import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserRepository userRepository;

    public Users getUserProfile(String userId, String token) throws AuthorizationFailedException, UserNotFoundException {

        UserAuth auth = userAuthRepository.findByAccessToken( token );
        if (auth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (auth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        Users user = userRepository.findByUuid( userId );

        if (user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return user;
    }
}
