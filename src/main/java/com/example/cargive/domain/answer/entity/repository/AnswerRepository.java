package com.example.cargive.domain.answer.entity.repository;

import com.example.cargive.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}