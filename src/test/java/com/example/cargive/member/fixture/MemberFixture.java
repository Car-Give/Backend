package com.example.cargive.member.fixture;

import com.example.cargive.domain.member.entity.Email;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.entity.Social;
import com.example.cargive.global.template.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberFixture {
    MEMBER_1("MEMBER_ID_1", "MEMBER_NAME_1", "010-1234-5678",
            Email.from("test1@test.com"), Social.NAVER, "ImageUrl", Status.NORMAL),
    MEMBER_2("MEMBER_ID_2", "MEMBER_NAME_2", "010-1234-5678",
            Email.from("test2@test.com"), Social.NAVER, "ImageUrl", Status.NORMAL)
    ;
    private final String loginId;
    private final String name;
    private final String phoneNumber;
    private final Email email;
    private final Social social;
    private final String imageUrl;
    private final Status status;

    public Member createEntity() {
        return new Member(loginId, name, phoneNumber, email, social, imageUrl);
    }
}
