package com.example.questionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.questionservice.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Basic CRUD operations are automatically provided by JpaRepository
}