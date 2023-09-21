package com.example.cargive.domain.question.service.response;

import lombok.Builder;

@Builder
public record LoadQuestionResponse(
        String title,

        String content,

        String categoryValue
) {
}
