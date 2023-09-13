package com.example.cargive.global.template;

import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status implements EnumStandard {
    NORMAL("정상"),
    EXPIRED("소멸")
    ;

    private final String value;

    @Converter
    public static class StatusConverter extends EnumConverter<Status> {
        public StatusConverter() {
            super(Status.class);
        }
    }
}