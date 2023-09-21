package com.example.cargive.global.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<BaseResponse> handleBaseException(BaseException e) {
        return BaseResponse.toResponseEntityContainsStatus(e.getErrorStatus());
    }
}
