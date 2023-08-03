package com.example.cargive.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //즐겨찾기
    FAVORITE_CREATE_SUCCESS("F001", "즐겨찾기 생성 성공"),
    FAVORITE_DELETE_SUCCESS("F002", "즐겨찾기 삭제 성공"),

    // 주차장
    PARKING_LOT_CREATE_SUCCESS("P001", "주차장 생성 성공"),
    PARKING_LOT_DELETE_SUCCESS("P002", "주차장 삭제 성공"),
    PARKING_LOT_READ_SUCCESS("P003", "주차장 조회 성공");

    private final String code;
    private final String message;
}
