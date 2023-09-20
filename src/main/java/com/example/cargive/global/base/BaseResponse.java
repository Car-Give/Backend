package com.example.cargive.global.base;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.cargive.global.base.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status", "code", "message", "result"})
public class BaseResponse<T> { // BaseResponse 객체를 사용할때 성공, 실패 경우
    private final HttpStatus status;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.status = SUCCESS.getStatus();
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 성공한 경우 (status를 추가로 받는 경우)
    public BaseResponse(BaseResponseStatusImpl status, T result) {
        this.status = status.getStatus();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }

    // 요청에 성공한 경우 (status만 받는 경우)
    public BaseResponse(BaseResponseStatusImpl status) {
        this.status = status.getStatus();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
