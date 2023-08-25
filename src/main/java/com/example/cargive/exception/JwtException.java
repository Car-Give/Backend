package com.example.cargive.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtException extends RuntimeException{

    private final String message;

    public JwtException(String message) {
        this.message = message;
    }
}
