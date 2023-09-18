package com.example.cargive.domain.question.entity;

import com.example.cargive.global.template.EnumConverter;
import com.example.cargive.global.template.EnumStandard;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @Converter
    public static class CategoryConverter extends EnumConverter<Category> {
        public CategoryConverter() { super(Category.class); }
    }
}
