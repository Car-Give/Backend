package com.example.cargive.domain.member.entity;

import com.example.cargive.global.template.Status;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String name;

    private String phoneNumber;

    @Embedded
    private Email email;

    @Convert(converter = Social.SocialConverter.class)
    private Social social;

    private String imageUrl;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @Builder
    public Member(String loginId, String name, String phoneNumber, Email email, Social social, String imageUrl) {
        this.loginId = loginId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.social = social;
        this.imageUrl = imageUrl;
        this.status = Status.NORMAL;
    }
}
