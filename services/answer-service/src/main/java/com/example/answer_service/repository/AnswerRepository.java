package com.example.answer_service.repository;

import com.example.answer_service.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerRepository extends MongoRepository<Answer, String> {
}
