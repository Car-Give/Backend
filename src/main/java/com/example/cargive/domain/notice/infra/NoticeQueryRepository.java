package com.example.cargive.domain.notice.infra;

import com.example.cargive.domain.notice.infra.dto.NoticeQueryResponse;

import java.util.List;

public interface NoticeQueryRepository {
    List<NoticeQueryResponse> getNoticeResponseList();
}