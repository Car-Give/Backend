package com.example.cargive.global.base;

import org.springframework.http.HttpStatus;

public interface BaseResponseStatusImpl {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
