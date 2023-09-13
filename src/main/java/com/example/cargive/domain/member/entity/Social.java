package com.example.cargive.domain.member.entity;

import com.example.cargive.global.template.EnumConverter;
import com.example.cargive.global.template.EnumStandard;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Social implements EnumStandard {
    GOOGLE("구글"),
    KAKAO("카카오"),
    NAVER("네이버"),
    ;

    private final String value;

    @Override
    public String getValue() { return value; }

    @Converter
    public static class SocialConverter extends EnumConverter<Social> {
        public SocialConverter() { super(Social.class); }
    }
}
