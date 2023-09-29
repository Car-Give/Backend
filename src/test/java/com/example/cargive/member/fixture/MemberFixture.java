package com.example.cargive.member.fixture;

import com.example.cargive.domain.member.entity.Email;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.entity.Social;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    WIZ("wiz123", "wiz", "010-0123-4567", "wiz@example.com", Social.KAKAO, null),
    ASSAC("assac1569", "assac", "010-1234-5678", "assac@example.com", Social.NAVER, null)
    ;

    private final String loginId;
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final Social social;
    private final String imageUrl;

    public Member toMember() {
        return Member.builder()
                .loginId(loginId)
                .name(name)
                .phoneNumber(phoneNumber)
                .email(Email.from(email))
                .social(social)
                .imageUrl(imageUrl)
                .build();
    }
}
