package com.example.cargive.domain.notice.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NoticeRequest(
        @NotBlank(message = "공지 사항의 제목을 입력해주세요")
        String title,
        @NotBlank(message = "공지 사항의 내용을 입력해주세요")
        String content
) {
}
