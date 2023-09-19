package com.example.cargive.domain.parkingLot.exception;

import com.example.cargive.global.base.BaseResponseStatusImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// ParkingLot Entity의 오류들을 관리
@Getter
@RequiredArgsConstructor
public enum ParkingLotErrorStatus implements BaseResponseStatusImpl {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
