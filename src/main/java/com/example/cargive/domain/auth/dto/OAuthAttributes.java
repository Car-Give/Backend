package com.example.cargive.domain.auth.dto;

import com.example.cargive.domain.member.entity.Social;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attribute) -> {
        String name = (String)attribute.get("name");
        String email = (String)attribute.get("email");
        String imageUrl = (String) attribute.get("picture");

        return new UserProfile(name, email, imageUrl, Social.GOOGLE);
    }),

    NAVER("naver", (attribute) -> {
        Map<String, String> profile = (Map<String, String>) attribute.get("response");

        String email = profile.get("email");
        String name = profile.get("name");
        String imageUrl = profile.get("profile_image");
        String phoneNumber = profile.get("mobile");

        return new UserProfile(name, email, imageUrl, Social.NAVER, phoneNumber);
    }),

    KAKAO("kakao", (attribute) -> {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = profile.get("nickname");
        String imageUrl = profile.get("profile_image_url");

        return new UserProfile(name, email, imageUrl, Social.KAKAO);
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}