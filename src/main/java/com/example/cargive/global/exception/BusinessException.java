package com.example.cargive.global.exception;

import com.example.cargive.global.base.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final BaseResponseStatus globalErrorCode;

    public BusinessException(BaseResponseStatus globalErrorCode) {
        super(globalErrorCode.getMessage());
        this.globalErrorCode = globalErrorCode;
    }
}

