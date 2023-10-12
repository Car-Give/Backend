package com.example.cargive.domain.notice.controller.dto.response;

import com.example.cargive.domain.notice.entity.Notice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponse {
    private String title;
    private String content;
    private LocalDateTime createAt;

    public static NoticeResponse toDto(Notice notice) {
        return new NoticeResponse(notice.getTitle(), notice.getContent(), notice.getCreateAt());
    }
}
