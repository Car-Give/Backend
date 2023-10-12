package com.example.cargive.domain.notice.controller;

import com.example.cargive.domain.notice.controller.dto.request.NoticeRequest;
import com.example.cargive.domain.notice.service.NoticeService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<BaseResponse> getNoticeList() {
        return BaseResponse.toResponseEntityContainsResult(noticeService.getNoticeList());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<BaseResponse> getNoticeInfo(@PathVariable Long noticeId) {
        return BaseResponse.toResponseEntityContainsResult(noticeService.getNoticeInfo(noticeId));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createNotice(@RequestBody @Valid NoticeRequest request) {
        noticeService.createNotice(request);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<BaseResponse> editNotice(@RequestBody @Valid NoticeRequest request,
                                                   @PathVariable Long noticeId) {
        noticeService.editNotice(request, noticeId);
        return BaseResponse.toResponseEntityContainsResult(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<BaseResponse> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
