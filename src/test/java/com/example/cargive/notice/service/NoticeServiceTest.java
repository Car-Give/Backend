package com.example.cargive.notice.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.notice.controller.dto.request.NoticeRequest;
import com.example.cargive.domain.notice.controller.dto.response.NoticeResponse;
import com.example.cargive.domain.notice.entity.Notice;
import com.example.cargive.domain.notice.infra.dto.NoticeQueryResponse;
import com.example.cargive.domain.notice.service.NoticeService;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.cargive.notice.fixture.NoticeFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Notice [Service Layer] -> NoticeService 테스트")
public class NoticeServiceTest extends ServiceTest {
    @Autowired
    private NoticeService noticeService;
    private Notice notice;
    private Long noticeId;
    private Long errorNoticeId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        notice = NOTICE_1.createEntity();

        noticeId = noticeRepository.save(notice).getId();
    }

    @AfterEach
    public void afterTest() {
        noticeRepository.deleteAll();
    }

    @Nested
    @DisplayName("공지사항 목록 조회")
    public class getNoticeListTest {
        @Test
        @DisplayName("데이터를 조회한 결과가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when
            noticeRepository.deleteAll();

            // then
            assertThatThrownBy(() -> noticeService.getNoticeList())
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetNoticeList() {
            // when
            List<NoticeQueryResponse> responseList = noticeService.getNoticeList();

            // then
            Assertions.assertThat(responseList.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("공지사항 상세 조회")
    public class getNoticeInfoTest {
        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() {
            // when - then
            assertThatThrownBy(() -> noticeService.getNoticeInfo(errorNoticeId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 단일 조회에 성공한다")
        public void successGetNoticeInfo() {
            // when
            NoticeResponse noticeInfo = noticeService.getNoticeInfo(noticeId);

            // then
            assertAll(
                    () -> assertThat(noticeInfo.getContent()).isEqualTo(notice.getContent()),
                    () -> assertThat(noticeInfo.getTitle()).isEqualTo(notice.getTitle())
            );
        }
    }

    @Nested
    @DisplayName("공지사항 작성")
    public class createNoticeTest {
        @Test
        @DisplayName("데이터 생성에 성공한다")
        public void successCreateNotice() {
            // given
            NoticeRequest request = getNoticeRequest();

            // when
            noticeService.createNotice(request);

            // then
            assertThat(noticeRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("공지사항 수정")
    public class editNoticeTest {
        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() {
            // given
            NoticeRequest request = getNoticeRequest();

            // when - then
            assertThatThrownBy(() -> noticeService.editNotice(request, errorNoticeId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 수정에 성공한다")
        public void successEditNotice() {
            // given
            NoticeRequest request = getNoticeRequest();

            // when
            noticeService.editNotice(request, noticeId);

            // then
            assertAll(
                    () -> assertThat(notice.getTitle()).isEqualTo(request.title()),
                    () -> assertThat(notice.getContent()).isEqualTo(request.content())
            );
        }
    }

    @Nested
    @DisplayName("공지사항 삭제")
    public class deleteNoticeTest {
        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() {
            // when - then
            assertThatThrownBy(() -> noticeService.deleteNotice(errorNoticeId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 삭제에 성공한다")
        public void successDeleteNotice() {
            // when
            noticeService.deleteNotice(noticeId);

            // then
            assertThat(notice.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private NoticeRequest getNoticeRequest() {
        return new NoticeRequest("Notice Title", "Notice Content");
    }
}