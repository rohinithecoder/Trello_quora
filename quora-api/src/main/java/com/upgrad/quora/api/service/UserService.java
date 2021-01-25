package com.upgrad.quora.api.service;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.upgrad.quora.api.requestresponse.SigninResponse;
import com.upgrad.quora.api.requestresponse.SignupUserRequest;
import com.upgrad.quora.service.business.JwtTokenProvider;
import com.upgrad.quora.service.business.PasswordCryptographyProvider;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.Users;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public Users signUpUser(SignupUserRequest signUpRequest) throws SignUpRestrictedException {
        if (userRepository.findByUserName( signUpRequest.getUser_name() ) != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        if (userRepository.findByEmail( signUpRequest.getEmail_address()  ) != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String[] encrypted = passwordCryptographyProvider.encrypt( signUpRequest.getPassword() );

        Users user = new Users();

        user.setUuid( UUID.randomUUID().toString() );
        user.setFirstName( signUpRequest.getFirst_name() );
        user.setLastName( signUpRequest.getLast_name() );
        user.setUserName( signUpRequest.getUser_name() );
        user.setEmail( signUpRequest.getEmail_address() );
        user.setPassword( encrypted[1] );
        user.setSalt( encrypted[0] );
        user.setRole( "nonadmin" );

        Users newUser = userRepository.save( user );


        return newUser;
    }

    public Map<String, Object> signInUser(String credentials) throws Base64DecodingException, AuthenticationFailedException {

        System.out.println(credentials);
        // Extract username and password
        String decodedCredentials = new String( Base64Utils.decodeFromString(credentials.split( " " )[1]));

        String username = decodedCredentials.split( ":" )[0];
        String password = decodedCredentials.split( ":" )[1];

        Users dbUser = userRepository.findByUserName( username );
        if(dbUser == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        if (!passwordCryptographyProvider.encrypt( password, dbUser.getSalt() ).equals( dbUser.getPassword() )) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }

        JwtTokenProvider tokenProvider = new JwtTokenProvider( "MY_SECRET" );
        ZonedDateTime expiryDate = ZonedDateTime.now().plusDays( 7 );
        String token = tokenProvider.generateToken( dbUser.getUuid().toString(), ZonedDateTime.now(), expiryDate);

        UserAuth auth = new UserAuth();
        auth.setUuid( dbUser.getUuid() );
        auth.setAccessToken( token );
        auth.setUserId( dbUser );
        auth.setExpiresAt( expiryDate.toLocalDateTime() );
        auth.setLoginAt( ZonedDateTime.now().toLocalDateTime() );
        userAuthRepository.save( auth );


        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("access_token", token);
        signInResponse.put("signInResponse",  new SigninResponse( dbUser.getUuid(), "SIGNED IN SUCCESSFULLY"));
        return signInResponse;
    }

    public UserAuth signOutUser(String token) throws SignOutRestrictedException {

        UserAuth auth = userAuthRepository.findByAccessToken( token );

        if (auth == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        auth.setLogoutAt( LocalDateTime.now() );

        return userAuthRepository.save( auth );
    }
}
