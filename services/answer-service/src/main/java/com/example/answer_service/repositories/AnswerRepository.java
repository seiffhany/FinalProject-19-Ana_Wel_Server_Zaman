package com.example.answer_service.repositories;

import com.example.answer_service.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, UUID> {
}
