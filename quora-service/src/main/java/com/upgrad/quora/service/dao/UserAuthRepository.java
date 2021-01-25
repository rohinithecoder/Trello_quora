package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.data.repository.CrudRepository;

public interface UserAuthRepository extends CrudRepository<UserAuth, Long> {
    public UserAuth findByAccessToken(String token);
}
