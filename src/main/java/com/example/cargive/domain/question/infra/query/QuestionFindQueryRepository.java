package com.example.cargive.domain.question.infra.query;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.service.response.GetListOfQuestionResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionFindQueryRepository {
    List<GetListOfQuestionResponse> findPageOrderByCreateAt(Member member, LocalDateTime createAt);
}
