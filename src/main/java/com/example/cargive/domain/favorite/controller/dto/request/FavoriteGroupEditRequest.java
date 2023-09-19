package com.example.cargive.domain.favorite.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

// 즐겨찾기 그룹의 이름을 변경하기 위한 DTO 파일
public record FavoriteGroupEditRequest(
        @NotBlank(message = "수정할 그룹 이름을 입력해주세요")
        String name
) {
}