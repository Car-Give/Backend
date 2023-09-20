package com.example.cargive.domain.favorite.exception;

import com.example.cargive.global.base.BaseResponseStatusImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// Favorite Entity의 오류들을 관리
@Getter
@RequiredArgsConstructor
public enum FavoriteErrorStatus implements BaseResponseStatusImpl {
    NOT_FOUND_SORT_CONDITION(HttpStatus.NOT_FOUND, "F002", "지원하지 않는 정렬 방식입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}