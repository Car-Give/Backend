package com.example.cargive.answer.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.answer.controller.dto.AnswerRequest;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Answer [Controller Layer] -> AnswerController 테스트")
public class AnswerControllerTest extends ControllerTest {
    @Nested
    @DisplayName("답변 생성 API [POST /api/answer/{questionId}]")
    public class createAnswerTest {
        private final static String BASE_URL = "/api/answer/{questionId}";
        private final static Long questionId = 1L;
        private final static Long errorQuestionId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 questionId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidQuestionId() throws Exception {
            // given
            AnswerRequest answerRequest = getAnswerRequest();
            doThrow(new BaseException(BaseResponseStatus.QUESTION_NOT_FOUND_ERROR))
                    .when(answerService)
                    .createAnswer(answerRequest, errorQuestionId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, errorQuestionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(answerRequest));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.QUESTION_NOT_FOUND_ERROR;

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
                                    "Answer/Create/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("답변 생성에 성공한다")
        public void successCreateAnswer() throws Exception {
            // given
            AnswerRequest answerRequest = getAnswerRequest();
            doNothing()
                    .when(answerService)
                    .createAnswer(answerRequest, questionId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, errorQuestionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(answerRequest));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isCreated()
                    ).andDo(
                            document(
                                    "Answer/Create/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())
                    )
                    );
        }
    }

    @Nested
    @DisplayName("답변 수정 API [PUT /api/answer/{answerId}]")
    public class editAnswerTest {
        private final static String BASE_URL = "/api/answer/{answerId}";
        private final static long answerId = 1L;
        private final static long errorAnswerId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 answerId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidAnswerId() throws Exception {
            // given
            AnswerRequest answerRequest = getAnswerRequest();
            doThrow(new BaseException(BaseResponseStatus.ANSWER_NOT_FOUND_ERROR))
                    .when(answerService)
                    .editAnswer(answerRequest, errorAnswerId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, errorAnswerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(answerRequest));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.ANSWER_NOT_FOUND_ERROR;

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
                                    "Answer/Edit/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("답변 수정에 성공한다")
        public void successEditAnswerTest() throws Exception {
            // given
            AnswerRequest answerRequest = getAnswerRequest();
            doNothing()
                    .when(answerService)
                    .editAnswer(answerRequest, answerId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, answerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(answerRequest));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isOk()
                    ).andDo(
                            document(
                                    "Answer/Edit/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()))
                    );
        }
    }

    @Nested
    @DisplayName("답변 삭제 API [DELETE /api/answer/{answerId}]")
    public class deleteAnswerTest {
        private final static String BASE_URL = "/api/answer/{answerId}";
        private final static Long answerId = 1L;
        private final static Long errorAnswerId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 answerId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidAnswerId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.ANSWER_NOT_FOUND_ERROR))
                    .when(answerService)
                    .deleteAnswer(errorAnswerId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorAnswerId);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.ANSWER_NOT_FOUND_ERROR;

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
                                    "Answer/Delete/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("답변 삭제에 성공한다")
        public void successDeleteAnswer() throws Exception {
            // given
            doNothing()
                    .when(answerService)
                    .deleteAnswer(answerId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, answerId);

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isNoContent()
                    ).andDo(
                            document(
                                    "Answer/Delete/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())
                            )
                    );
        }
    }

    private AnswerRequest getAnswerRequest() {
        return new AnswerRequest("Content");
    }
}