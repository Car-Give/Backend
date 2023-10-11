package com.example.cargive.notice.domain;

import com.example.cargive.domain.notice.entity.Notice;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.notice.fixture.NoticeFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Notice 도메인 테스트")
public class NoticeTest {
    private Notice notice;

    @BeforeEach
    public void initTest() {
        notice = NOTICE_1.createEntity();
    }

    @Test
    @DisplayName("Notice 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(notice.getTitle()).isEqualTo(NOTICE_1.getTitle()),
                () -> assertThat(notice.getContent()).isEqualTo(NOTICE_1.getContent()),
                () -> assertThat(notice.getStatus()).isEqualTo(Status.NORMAL)
        );
    }

    @Test
    @DisplayName("editInfo() 메서드 테스트")
    public void editInfoTest() {
        // when
        notice.editInfo(NOTICE_2.getTitle(), NOTICE_2.getContent());

        // then
        assertAll(
                () -> assertThat(notice.getTitle()).isEqualTo(NOTICE_2.getTitle()),
                () -> assertThat(notice.getContent()).isEqualTo(NOTICE_2.getContent())
        );
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        // when
        notice.deleteEntity();

        // then
        assertThat(notice.getStatus()).isEqualTo(Status.EXPIRED);
    }
}