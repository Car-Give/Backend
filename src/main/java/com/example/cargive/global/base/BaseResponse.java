package com.example.cargive.global.base;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import static com.example.cargive.global.base.BaseResponseStatus.SUCCESS;

@Data
@Builder
@JsonPropertyOrder({"status", "code", "message", "result"})
public class BaseResponse { // BaseResponse 객체를 사용할때 성공, 실패 경우
    private final int status;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

    // Custom Status를 포함한 Response
    public static ResponseEntity<BaseResponse> toResponseEntityContainsStatus(BaseResponseStatus baseResponseStatus) {
        return ResponseEntity
                .status(baseResponseStatus.getStatus())
                .body(BaseResponse.builder()
                        .status(baseResponseStatus.getStatus().value())
                        .code(baseResponseStatus.getCode())
                        .message(baseResponseStatus.getMessage())
                        .build());
    }

    // Http 200, Result를 포함한 Response
    public static ResponseEntity<BaseResponse> toResponseEntityContainsResult(Object result) {
        return ResponseEntity
                .status(SUCCESS.getStatus())
                .body(BaseResponse.builder()
                        .status(SUCCESS.getStatus().value())
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .result(result)
                        .build());
    }

    // Custom Status, Result를 포함한 Response
    public static ResponseEntity<BaseResponse> toResponseEntityContainsStatusAndResult(BaseResponseStatus baseResponseStatus, Object result) {
        return ResponseEntity
                .status(baseResponseStatus.getStatus())
                .body(BaseResponse.builder()
                        .status(baseResponseStatus.getStatus().value())
                        .code(baseResponseStatus.getCode())
                        .message(baseResponseStatus.getMessage())
                        .result(result)
                        .build());
    }
}
