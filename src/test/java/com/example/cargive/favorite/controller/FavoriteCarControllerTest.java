package com.example.cargive.favorite.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("FavoriteCar [Controller Layer] -> FavoriteCarController 테스트")
public class FavoriteCarControllerTest extends ControllerTest {
    @Nested
    @DisplayName("차량 즐겨찾기 등록 API [POST /api/favorite/car/{carId}]")
    class createFavoriteCarTest {
        private final static String BASE_URL = "/api/favorite/car/{carId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoriteCarService)
                    .createFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Create/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("유효하지 않은 carId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(favoriteCarService)
                    .createFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, errorCarId)
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Create/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("사용자의 정보와 차량의 정보가 일치하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByMemberValidation() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR))
                    .when(favoriteCarService)
                    .createFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Create/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("차량 즐겨찾기 등록에 성공한다")
        public void successCreateFavoriteCar() throws Exception {
            // given
            doNothing()
                    .when(favoriteCarService)
                    .createFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isCreated()
                    ).andDo(
                            document(
                                    "FavoriteCar/Create/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("차량 즐겨찾기 삭제 API [DELETE /api/favorite/car/{favoriteCarId}]")
    class deleteFavoriteCarTest {
        private final static String BASE_URL = "/api/favorite/car/{favoriteCarId}";
        private final static Long favoriteCarId = 1L;
        private final static Long memberId = 1L;
        private final static Long errorFavoriteCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;
        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoriteCarService)
                    .deleteFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteCarId)
                    .param("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Delete/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("favoriteCarId").description("차량 즐겨찾기 Id")
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
        @DisplayName("유효하지 않은 favoriteCarId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR))
                    .when(favoriteCarService)
                    .deleteFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorFavoriteCarId)
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Delete/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("favoriteCarId").description("차량 즐겨찾기 Id")
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
        @DisplayName("사용자의 정보와 즐겨찾기의 정보가 일치하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByMemberValidation() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR))
                    .when(favoriteCarService)
                    .deleteFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteCarId)
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoriteCar/Delete/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("favoriteCarId").description("차량 즐겨찾기 Id")
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
        @DisplayName("즐겨찾기 삭제에 성공한다")
        public void successDeleteFavoriteCar() throws Exception {
            // given
            doNothing()
                    .when(favoriteCarService)
                    .deleteFavoriteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteCarId)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isNoContent())
                    .andDo(
                            document(
                                    "FavoriteCar/Delete/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("favoriteCarId").description("차량 즐겨찾기 Id")
                                    )
                            )
                    );
        }
    }
}