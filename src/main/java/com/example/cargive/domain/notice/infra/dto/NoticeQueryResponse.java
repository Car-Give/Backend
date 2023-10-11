package com.example.cargive.domain.notice.infra.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeQueryResponse {
    private String title;
    private LocalDateTime createAt;

    @QueryProjection
    public NoticeQueryResponse(String title, LocalDateTime createAt) {
        this.title = title;
        this.createAt = createAt;
    }
}
