package com.example.cargive.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotSnsTypeException extends RuntimeException {

    private final String message;

    public NotSnsTypeException(String message) {
        this.message = message;
    }
}
