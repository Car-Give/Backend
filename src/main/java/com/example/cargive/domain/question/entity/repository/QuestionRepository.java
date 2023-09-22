package com.example.cargive.domain.question.entity.repository;

import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.domain.question.infra.query.QuestionFindQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionFindQueryRepository {
}
