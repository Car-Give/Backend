package com.example.cargive.domain.auth.dto;

import com.example.cargive.domain.member.entity.Email;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.entity.Social;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {
    private String loginId;
    private String name;
    private Email email;
    private Social social;
    private String imageUrl;
    private String phoneNumber;

    public UserProfile(String name, String email, String imageUrl, Social social) {
        this.loginId = getMemberLoginId(email, social);
        this.name = name;
        this.email = Email.from(email);
        this.social = social;
        this.imageUrl = imageUrl;
        this.phoneNumber = "";
    }

    public UserProfile(String name, String email, String imageUrl, Social social, String phoneNumber) {
        this.loginId = getMemberLoginId(email, social);
        this.name = name;
        this.email = Email.from(email);
        this.social = social;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
    }

    public Member createMemberEntity() {
        return Member.builder()
                .loginId(loginId)
                .name(name)
                .email(email)
                .social(social)
                .imageUrl(imageUrl)
                .phoneNumber(phoneNumber)
                .build();
    }

    private String getMemberLoginId(String email, Social social) {
        return social.getValue() + "-" + email.substring(0, email.indexOf("@"));
    }
}
