package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<Users, UUID> {

    public Users findByFirstName(String firstName);
    public Users findByUserName(String userName);
    public Users findByEmail(String email);
    public Users findByUuid(String uuid);

}
