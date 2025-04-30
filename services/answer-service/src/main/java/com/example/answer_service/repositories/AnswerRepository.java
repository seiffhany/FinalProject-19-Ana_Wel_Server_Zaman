package com.example.answer_service.repositories;

import com.example.answer_service.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, UUID> {
    List<Answer> findByParentID(UUID parentID);
    Answer findAnswerById(UUID id);
    List<Answer> findByUserId(UUID userId);
    List<Answer> findByQuestionID(UUID questionID);

}
