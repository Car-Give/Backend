package com.example.cargive.domain.member.controller.dto.response;

import com.example.cargive.domain.member.entity.Email;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.entity.Social;

public record MemberResponse(
        String loginId,
        String name,
        String phoneNumber,
        Email email,
        Social social,
        String imageUrl
) {
    public static MemberResponse toDto(Member member) {
        return new MemberResponse(member.getLoginId(), member.getName(), member.getPhoneNumber(),
                member.getEmail(), member.getSocial(), member.getImageUrl());
    }
}
