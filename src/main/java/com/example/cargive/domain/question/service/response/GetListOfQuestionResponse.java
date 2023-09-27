package com.example.cargive.domain.question.service.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record GetListOfQuestionResponse(
        Long id,
        String title,
        LocalDateTime createAt
) {
    @QueryProjection
    public GetListOfQuestionResponse {}
}