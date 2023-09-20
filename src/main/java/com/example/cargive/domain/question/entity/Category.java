package com.example.cargive.domain.question.entity;

import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.template.EnumConverter;
import com.example.cargive.global.template.EnumStandard;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category implements EnumStandard {
    COMMON("이용 문의"),
    BUG("오류 신고"),
    SUGGEST("서비스 제안"),
    ETC("기타"),
    ;

    private final String value;

    @Override
    public String getValue() { return value; }

    public static Category from(String value) {
        return Arrays.stream(values())
                .filter(category -> category.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.QUESTION_CATEGORY_NOT_FOUND_ERROR));
    }

    @Converter
    public static class CategoryConverter extends EnumConverter<Category> {
        public CategoryConverter() { super(Category.class); }
    }
}
