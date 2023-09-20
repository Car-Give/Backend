package com.example.cargive.global.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String message;

    // 요청에 성공했지만 result가 주어지지 않았거나 요청에 실패한 경우
    public BaseException(BaseResponseStatusImpl status) {
        this.status = status.getStatus();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
