package com.example.cargive.notice.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.notice.controller.dto.request.NoticeRequest;
import com.example.cargive.domain.notice.controller.dto.response.NoticeResponse;
import com.example.cargive.domain.notice.infra.dto.NoticeQueryResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.cargive.common.ApiDocumentUtils.getDocumentRequest;
import static com.example.cargive.common.ApiDocumentUtils.getDocumentResponse;
import static com.example.cargive.notice.fixture.NoticeFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Notice [Controller Layer] -> NoticeController 테스트")
public class NoticeControllerTest extends ControllerTest {
    @Nested
    @DisplayName("공지사항 목록 조회 API [GET /api/notice]")
    public class getNoticeListTest {
        private final String BASE_URL = "/api/notice";

        @Test
        @DisplayName("데이터를 조회한 결과가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.NOTICE_LIST_EMPTY_ERROR))
                    .when(noticeService)
                    .getNoticeList();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders.get(BASE_URL);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.NOTICE_LIST_EMPTY_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Notice/List/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetNoticeList() throws Exception {
            // given
            List<NoticeQueryResponse> responseList = getNoticeQueryResponseList();

            doReturn(responseList)
                    .when(noticeService)
                    .getNoticeList();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders.get(BASE_URL);

            // then
            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.size()").value(1)
                    ).andDo(
                            document(
                                    "Notice/List/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result[0].title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                            fieldWithPath("result[0].createAt").type(JsonFieldType.STRING).description("공지사항 생성 일자")
                                    )));
        }
    }

    @Nested
    @DisplayName("공지사항 상세 조회 API [GET /api/notice/{noticeId}]")
    public class getNoticeInfoTest {
        private static final String BASE_URL = "/api/notice/{noticeId}";
        private static final Long noticeId = 1L;
        private static final Long errorNoticeId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.NOTICE_NOT_FOUND_ERROR))
                    .when(noticeService)
                    .getNoticeInfo(errorNoticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, errorNoticeId);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.NOTICE_NOT_FOUND_ERROR;

            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Notice/Info/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("데이터 단일 조회에 성공한다")
        public void successGetNoticeInfo() throws Exception {
            // given
            NoticeResponse response = NoticeResponse.toDto(NOTICE_1.createEntity());

            doReturn(response)
                    .when(noticeService)
                    .getNoticeInfo(noticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, noticeId);

            // then
            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isOk()
                    ).andDo(
                            document(
                                    "Notice/Info/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result.title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                            fieldWithPath("result.content").type(JsonFieldType.STRING).description("공지사항 내용"),
                                            fieldWithPath("result.createAt").type(JsonFieldType.NULL).description("공지사항 작성 일자"))
                            )
                    );
        }
    }

    @Nested
    @DisplayName("공지사항 작성 API [POST /api/notice]")
    public class createNoticeTest {
        private final static String BASE_URL = "/api/notice";
        @Test
        @DisplayName("데이터 생성에 성공한다")
        public void successCreateNotice() throws Exception {
            // given
            NoticeRequest noticeRequest = getNoticeRequest();

            doNothing()
                    .when(noticeService)
                    .createNotice(noticeRequest);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noticeRequest));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isCreated()
                    ).andDo(
                            document(
                                    "Notice/Create/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                            fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("공지사항 수정 API [PUT /api/notice/{noticeId}]")
    public class editNoticeTest {
        private final static String BASE_URL = "/api/notice/{noticeId}";
        private final static Long noticeId = 1L;
        private final static Long errorNoticeId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() throws Exception {
            // given
            NoticeRequest noticeRequest = getNoticeRequest();

            doThrow(new BaseException(BaseResponseStatus.NOTICE_NOT_FOUND_ERROR))
                    .when(noticeService)
                    .editNotice(noticeRequest, errorNoticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, errorNoticeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noticeRequest));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.NOTICE_NOT_FOUND_ERROR;

            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Notice/Edit/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                            fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("데이터 수정에 성공한다")
        public void successEditNotice() throws Exception {
            // given
            NoticeRequest noticeRequest = getNoticeRequest();

            doNothing()
                    .when(noticeService)
                    .editNotice(noticeRequest, noticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, noticeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noticeRequest));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isOk()
                    ).andDo(
                            document(
                                    "Notice/Edit/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse()
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                                            fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("공지사항 삭제 API [DELETE /api/notice/{noticeId}]")
    public class deleteNoticeTest {
        private final static String BASE_URL = "/api/notice/{noticeId}";
        private final static Long noticeId = 1L;
        private final static Long errorNoticeId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 noticeId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidNoticeId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.NOTICE_NOT_FOUND_ERROR))
                    .when(noticeService)
                    .deleteNotice(errorNoticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorNoticeId);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.NOTICE_NOT_FOUND_ERROR;

            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Notice/Delete/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("데이터 삭제에 성공한다")
        public void successDeleteNotice() throws Exception {
            // given
            doNothing()
                    .when(noticeService)
                            .deleteNotice(noticeId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, noticeId);

            // then
            mockMvc
                    .perform(request)
                    .andExpect(
                            status().isNoContent()
                    ).andDo(
                            document(
                                    "Notice/Delete/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse()),
                                    pathParameters(
                                            parameterWithName("noticeId").description("공지사항 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }
    }

    private List<NoticeQueryResponse> getNoticeQueryResponseList() {
        List<NoticeQueryResponse> responseList = new ArrayList<>();
        responseList.add(new NoticeQueryResponse("Title", LocalDateTime.now()));

        return responseList;
    }

    private NoticeRequest getNoticeRequest() {
        return new NoticeRequest("Notice Title", "Notice Content");
    }
}
