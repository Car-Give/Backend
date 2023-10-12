package com.example.cargive.domain.answer.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(
        @NotBlank(message = "답변 내용을 입력해주세요")
        String content
) {
}