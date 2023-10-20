package com.example.cargive.question.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.question.controller.dto.request.SaveQuestionRequest;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.cargive.common.ApiDocumentUtils.getDocumentRequest;
import static com.example.cargive.common.ApiDocumentUtils.getDocumentResponse;
import static com.example.cargive.question.fixture.QuestionFixture.QUESTION_0;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Question [Controller Layer] -> QuestionController 테스트")
public class QuestionControllerTest extends ControllerTest {
    private static final Long ERROR_QUESTION_ID = Long.MAX_VALUE;

    @Nested
    @DisplayName("질문 생성 API [POST /api/question]")
    class SaveQuestion {
        private static final String BASE_URL = "/api/question";
        private static final Long MEMBER_ID = 1L;
        private static final Long BOARD_ID= 2L;

        @Test
        @DisplayName("질문 생성에 성공한다")
        void success() throws Exception {
            // given
            doReturn(BOARD_ID)
                    .when(questionService)
                    .saveQuestion(anyLong(), any());

            // when
            final SaveQuestionRequest request = getSaveQuestionRequest();

            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("memberId", String.valueOf(MEMBER_ID))
                    ;

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(
                            status().isCreated()
                    )
                    .andDo(
                            document(
                                    "Question/Save/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 ID")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
                                            fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용"),
                                            fieldWithPath("categoryValue").type(JsonFieldType.STRING).description("질문 카테고리")
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
    @DisplayName("질문 삭제 API [DELETE /api/question/{questionId}]")
    class DeleteQuestion {
        private static final String BASE_URL = "/api/question/{questionId}";
        private static final Long MEMBER_ID = 1L;
        private static final Long QUESTION_ID = 2L;

        @Test
        @DisplayName("유효하지 않은 질문 ID를 입력받으면 질문 삭제에 실패한다")
        void throwExceptionByInvalidQuestionId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.QUESTION_NOT_FOUND_ERROR))
                    .when(questionService)
                    .deleteQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, ERROR_QUESTION_ID)
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
                                    "Question/Delete/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
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
        @DisplayName("사용자가 작성한 질문이 아니면 질문 삭제에 실패한다")
        void throwExceptionByNotMatchMemberAndQuestion() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.QUESTION_MEMBER_NOT_MATCH_ERROR))
                    .when(questionService)
                    .deleteQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, ERROR_QUESTION_ID)
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
                                    "Question/Delete/Failure/Case2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
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
        @DisplayName("질문 삭제에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(questionService)
                    .deleteQuestion(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, QUESTION_ID)
                    .param("memberId", String.valueOf(MEMBER_ID))
                    ;

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(
                            status().isNoContent()
                    )
                    .andDo(
                            document(
                                    "Question/Delete/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("questionId").description("질문 ID")
                                    )
                            )
                    );
        }
    }

    private SaveQuestionRequest getSaveQuestionRequest() {
        return new SaveQuestionRequest(QUESTION_0.getTitle(), QUESTION_0.getContent(), QUESTION_0.getCategory().getValue());
    }
}
