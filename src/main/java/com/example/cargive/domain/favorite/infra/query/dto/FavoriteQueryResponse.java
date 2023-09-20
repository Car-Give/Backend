package com.example.cargive.domain.favorite.infra.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

// querydsl을 통한 데이터 결과값 반환에 사용되는 DTO 파일
@Getter
@NoArgsConstructor
public class FavoriteQueryResponse<T> {
    private boolean hasNext;
    private List<T> favoriteList = new ArrayList<>();

    public FavoriteQueryResponse(List<T> content, Pageable pageable) {
        this.hasNext = hasNext(content, pageable);
        this.favoriteList = content;
    }

    private boolean hasNext(List<T> content, Pageable pageable) {
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }
}