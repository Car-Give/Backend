package com.example.cargive.domain.favorite.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 즐겨찾기 그룹을 조회하는 경우, 직접적으로 Entity가 노출되는 것을 피하기 위해 별도의 DTO 파일 생성
@Getter
@NoArgsConstructor
public class FavoritePkGroupResponse {
    private Long id; // 즐겨찾기 그룹의 PK
    private String name; // 즐겨찾기 그룹의 이름
    private LocalDateTime createAt; // 즐겨찾기 그룹을 최신순으로 정렬하기 위한 필드

    @QueryProjection // querydsl을 위한 어노테이션 삽입
    public FavoritePkGroupResponse(Long id, String name, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
    }
}
