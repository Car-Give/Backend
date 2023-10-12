package com.example.cargive.notice.fixture;

import com.example.cargive.domain.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeFixture {
    NOTICE_1("Test Title1", "Test Content1"),
    NOTICE_2("Test Title2", "Test Content2");
    private final String title;
    private final String content;

    public Notice createEntity() {
        return new Notice(this.title, this.content);
    }
}