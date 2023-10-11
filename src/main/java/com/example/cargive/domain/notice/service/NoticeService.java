package com.example.cargive.domain.notice.service;

import com.example.cargive.domain.notice.controller.dto.request.NoticeRequest;
import com.example.cargive.domain.notice.controller.dto.response.NoticeResponse;
import com.example.cargive.domain.notice.entity.Notice;
import com.example.cargive.domain.notice.entity.repository.NoticeRepository;
import com.example.cargive.domain.notice.infra.dto.NoticeQueryResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeQueryResponse> getNoticeList() {
        return getNoticeResponseList();
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNoticeInfo(Long noticeId) {
        Notice findNotice = getNotice(noticeId);

        return NoticeResponse.toDto(findNotice);
    }

    @Transactional
    public void createNotice(NoticeRequest request) {
        Notice notice = createNoticeEntity(request);

        noticeRepository.save(notice);
    }

    @Transactional
    public void editNotice(NoticeRequest request, Long noticeId) {
        Notice findNotice = getNotice(noticeId);

        findNotice.editInfo(request.title(), request.content());
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice findNotice = getNotice(noticeId);

        findNotice.deleteEntity();
    }

    private List<NoticeQueryResponse> getNoticeResponseList() {
        List<NoticeQueryResponse> responseList = noticeRepository.getNoticeResponseList();

        if(responseList.isEmpty()) throw new BaseException(BaseResponseStatus.NOTICE_LIST_EMPTY_ERROR);

        return responseList;
    }

    private Notice createNoticeEntity(NoticeRequest request) {
        return new Notice(request.title(), request.content());
    }

    private Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOTICE_NOT_FOUND_ERROR));
    }
}
