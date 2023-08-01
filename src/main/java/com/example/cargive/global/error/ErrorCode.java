package com.example.cargive.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //server
    INTERNAL_SERVER_ERROR(500, "G001", "서버 오류"),
    INPUT_INVALID_VALUE(400, "G002", "잘못된 입력"),

    //Favorite
    FAVORITE_NOT_FOUND_ERROR(400, "F001", "존재하지 않는 즐겨찾기 입니다.");


    private final int status;
    private final String code;
    private final String message;

}
