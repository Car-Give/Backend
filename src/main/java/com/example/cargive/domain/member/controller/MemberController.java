package com.example.cargive.domain.member.controller;

import com.example.cargive.domain.member.controller.dto.request.MemberRequest;
import com.example.cargive.domain.member.service.MemberService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<BaseResponse> getMember(@RequestParam Long memberId) {
        return BaseResponse.toResponseEntityContainsResult(memberService.getMemberInfo(memberId));
    }

    @PutMapping
    public ResponseEntity<BaseResponse> editMember(@RequestParam Long memberId,
                                                   @RequestPart(name = "request") MemberRequest request,
                                                   @RequestPart(name = "file") MultipartFile file) throws IOException {
        memberService.editMember(memberId, request, file);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteMember(@RequestParam Long memberId) {
        memberService.deleteMember(memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
