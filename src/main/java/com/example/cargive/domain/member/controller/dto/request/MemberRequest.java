package com.example.cargive.domain.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank(message = "사용자의 전화번호를 입력해주세요")
        String phoneNumber
) {
}
