package com.upgrad.quora.api.service;

import com.upgrad.quora.api.requestresponse.UserDeleteResponse;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserRepository userRepository;

    public UserDeleteResponse userDelete(String userId, String token) throws AuthorizationFailedException, UserNotFoundException {

        UserAuth auth = userAuthRepository.findByAccessToken( token );

        if (auth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (auth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.");
        }

        if (auth.getUserId().getRole().equals( "nonadmin" )) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        Users user = userRepository.findByUuid( userId );

        if (user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        userRepository.delete( user );

        return new UserDeleteResponse( userId, "USER SUCCESSFULLY DELETED" );
    }
}
