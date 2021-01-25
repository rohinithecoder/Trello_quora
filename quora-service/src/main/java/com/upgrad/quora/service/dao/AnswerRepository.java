package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, UUID> {
    public Answer findByUuid(String uuid);
    public Iterable<Answer> findByQuestionId(Question question);
}
