package com.example.cargive.tag.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.domain.tag.infra.dto.TagQueryResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Tag [Controller Layer] -> TagController 테스트")
public class TagControllerTest extends ControllerTest {
    @Nested
    @DisplayName("차량에 등록된 차량 특징 카드 조회 API [GET /api/tag/{carId}]")
    class GetTagList {
        private static final String BASE_URL = "/api/tag/{carId}";
        private static final Long carId = 1L;

        @Test
        @DisplayName("차량에 등록된 차량 특징 카드가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.TAG_LIST_EMPTY_ERROR))
                    .when(tagService)
                    .getTagList(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, carId);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.TAG_LIST_EMPTY_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            MockMvcResultMatchers.status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Tag/List/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("carId").description("차량 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );

        }

        @Test
        @DisplayName("차량에 등록된 차량 특징 카드 조회에 성공한다")
        public void successToGetList() throws Exception {
            // given
            TagQueryResponse<TagResponse> response = new TagQueryResponse<>();
            doReturn(response)
                    .when(tagService)
                    .getTagList(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, carId);

            // then
            mockMvc.perform(request)
                    .andExpect(
                            MockMvcResultMatchers.status().isOk()
                    ).andDo(
                            document(
                                    "Tag/List/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("carId").description("차량 ID"))
                            )
                    );
        }
    }

    @Nested
    @DisplayName("차량 특징 카드 삭제 API [DELETE /api/tag/{tagId}]")
    class DeleteTag {
        private static final String BASE_URL = "/api/tag/{tagId}";
        private static final Long carId = 1L;
        private static final Long tagId = 1L;
        private static final Long errorTagId = Long.MAX_VALUE;
        private static final Long errorCarId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 tagId를 입력 받은 경우, 오류를 반환한다")
        public void throwExceptionByInvalidTagId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.TAG_NOT_FOUND_ERROR))
                    .when(tagService)
                    .deleteTag(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorTagId)
                    .param("carId", String.valueOf(carId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.TAG_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            MockMvcResultMatchers.status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Tag/Delete/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("tagId").description("차량 특징 카드 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("유효하지 않은 carId를 입력 받은 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(tagService)
                    .deleteTag(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, tagId)
                    .param("carId", String.valueOf(errorCarId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            MockMvcResultMatchers.status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Tag/Delete/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("tagId").description("차량 특징 카드 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("Tag와 Car Entity가 서로 연관된 관계가 아닐 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_MATCH_ERROR))
                    .when(tagService)
                    .deleteTag(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, tagId)
                    .param("carId", String.valueOf(carId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            MockMvcResultMatchers.status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "Tag/Delete/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("tagId").description("차량 특징 카드 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("차량 특징 카드 삭제에 성공한다")
        public void successToDelete() throws Exception {
            // given
            doNothing()
                    .when(tagService)
                    .deleteTag(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, tagId)
                    .param("carId", String.valueOf(carId));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            MockMvcResultMatchers.status().isNoContent()
                    ).andDo(
                            document(
                                    "Tag/Delete/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("tagId").description("차량 특징 카드 ID"))
                            )
                    );
        }
    }
}
