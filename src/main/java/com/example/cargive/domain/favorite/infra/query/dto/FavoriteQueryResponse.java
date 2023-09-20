package com.example.cargive.domain.favorite.infra.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// querydsl을 통한 데이터 결과값 반환에 사용되는 DTO 파일
@Getter
@NoArgsConstructor
public class FavoriteQueryResponse<T> {
    private List<T> favoriteList = new ArrayList<>();

    public FavoriteQueryResponse(List<T> content) {
        this.favoriteList = content;
    }
}