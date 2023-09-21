package com.example.cargive.question.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.question.service.response.LoadQuestionResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.cargive.question.fixture.QuestionFixture.QUESTION_0;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Question [Controller Layer] -> QuestionFindController 테스트")
public class QuestionFindControllerTest extends ControllerTest {
    private static final Long ERROR_QUESTION_ID = Long.MAX_VALUE;

    @Nested
    @DisplayName("질문 조회 API [GET /api/question/{questionId}]")
    class LoadQuestion {
        private static final String BASE_URL = "/api/question/{questionId}";
        private static final Long MEMBER_ID = 1L;
        private static final Long QUESTION_ID = 2L;

        @Test
        @DisplayName("유효하지 않은 질문 ID를 입력받으면 질문 조회에 실패한다")
        void throwExceptionByInvalidQuestionId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.QUESTION_NOT_FOUND_ERROR))
                    .when(questionFindService)
                    .loadQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL, ERROR_QUESTION_ID)
                    .param("memberId", String.valueOf(MEMBER_ID))
                    ;

            // then
            final BaseResponseStatus expectedError = BaseResponseStatus.QUESTION_NOT_FOUND_ERROR;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectedError.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "QuestionFind/GetDetail/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 ID")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("사용자가 작성한 질문이 아니면 질문 조회에 실패한다")
        void throwExceptionByNotMatchMemberAndQuestion() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.QUESTION_MEMBER_NOT_MATCH_ERROR))
                    .when(questionFindService)
                    .loadQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL, ERROR_QUESTION_ID)
                    .param("memberId", String.valueOf(MEMBER_ID))
                    ;

            // then
            final BaseResponseStatus expectedError = BaseResponseStatus.QUESTION_MEMBER_NOT_MATCH_ERROR;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectedError.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "QuestionFind/GetDetail/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 ID")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("질문 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(getLoadQuestionResponse())
                    .when(questionFindService)
                    .loadQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL, QUESTION_ID)
                    .param("memberId", String.valueOf(MEMBER_ID))
                    ;

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.title").exists(),
                            jsonPath("$.result.title").value(QUESTION_0.getTitle()),
                            jsonPath("$.result.content").exists(),
                            jsonPath("$.result.content").value(QUESTION_0.getContent())
                    )
                    .andDo(
                            document(
                                    "QuestionFind/GetDetail/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 ID")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result.title").type(JsonFieldType.STRING).description("질문 제목"),
                                            fieldWithPath("result.content").type(JsonFieldType.STRING).description("질문 내용"),
                                            fieldWithPath("result.categoryValue").type(JsonFieldType.STRING).description("질문 카테고리")
                                            )
                            )
                    );
        }
    }

    private LoadQuestionResponse getLoadQuestionResponse() {
        return LoadQuestionResponse.builder()
                .title(QUESTION_0.getTitle())
                .content(QUESTION_0.getContent())
                .categoryValue(QUESTION_0.getCategory().getValue())
                .build();
    }
}
