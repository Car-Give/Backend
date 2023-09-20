package com.example.cargive.domain.favorite.controller.dto.request;

import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

// 즐겨찾기 그룹을 생성하기 위한 DTO 파일
public record FavoriteGroupRequest(
        @NotBlank(message = "생성할 즐겨찾기명을 입력해주세요.")
        String name
) {
    public FavoritePkGroup toFavoriteGroup(Member member) {
        return FavoritePkGroup.builder()
                .name(name)
                .member(member)
                .build();
    }
}
