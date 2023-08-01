package com.example.cargive.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //즐겨찾기
    FAVORITE_CREATE_SUCCESS("F001", "즐겨찾기 생성 성공"),
    FAVORITE_DELETE_SUCCESS("F002", "즐겨찾기 삭제 성공");

    private final String code;
    private final String message;
}
