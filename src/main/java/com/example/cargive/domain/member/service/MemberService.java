package com.example.cargive.domain.member.service;

import com.example.cargive.domain.member.controller.dto.request.MemberRequest;
import com.example.cargive.domain.member.controller.dto.response.MemberResponse;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.service.S3Service;
import com.example.cargive.global.template.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private static final String S3_DIRECTORY_MEMBER = "member";

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long memberId) {
        Member findMember = getMember(memberId);
        checkValidation(findMember);

        return MemberResponse.toDto(findMember);
    }

    @Transactional
    public void editMember(Long memberId, MemberRequest request, MultipartFile file) throws IOException {
        Member findMember = getMember(memberId);
        checkValidation(findMember);

        editMemberInfo(findMember, request, file);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = getMember(memberId);
        checkValidation(findMember);

        findMember.deleteEntity();
    }

    private void checkValidation(Member member) {
        if(member.getStatus().equals(Status.EXPIRED))
            throw new BaseException(BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR);
    }

    private void editMemberInfo(Member member, MemberRequest request, MultipartFile file) throws IOException {
        String imageUrl = getImageUrl(file);
        member.editInfo(request.phoneNumber(), imageUrl);
    }

    private String getImageUrl(MultipartFile file) throws IOException {
        if(file.isEmpty()) return "";
        return s3Service.upload(file, S3_DIRECTORY_MEMBER);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }
}
