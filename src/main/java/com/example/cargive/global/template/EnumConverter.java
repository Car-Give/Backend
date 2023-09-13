package com.example.cargive.global.template;

import com.example.cargive.global.base.BaseException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.example.cargive.global.base.BaseResponseStatus.INVALID_ENUM;

@Converter
@Slf4j
public class EnumConverter<T extends EnumStandard> implements AttributeConverter<T , String> {

    private final Class<T> enumClass;

    public EnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @SneakyThrows
    @Override
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        T[] enumConstants = enumClass.getEnumConstants();
        for (T constant : enumConstants) {
            if (Objects.equals(constant.getValue(), dbData))
                return constant;
        }
        throw new BaseException(INVALID_ENUM);
    }
}

