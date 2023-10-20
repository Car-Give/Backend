package com.example.cargive.favorite.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.cargive.common.ApiDocumentUtils.getDocumentRequest;
import static com.example.cargive.common.ApiDocumentUtils.getDocumentResponse;
import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("FavoritePkInfo [Controller Layer] -> FavoritePkInfoController 테스트")
public class FavoritePkInfoControllerTest extends ControllerTest {
    @Nested
    @DisplayName("즐겨찾기된 주차장 조회 API [GET /api/favorite/info/{favoriteGroupId}]")
    class getFavoritePkInfosTest {
        private static final String BASE_URL = "/api/favorite/info/{favoriteGroupId}";
        private static final Long memberId = 1L;
        private static final Long cursorId = 10L;
        private static final Long favoriteGroupId = 1L;
        private static final Long favoriteInfoId = 1L;

        @Test
        @DisplayName("조회한 결과가 존재하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByEmptyResponse() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .getFavoriteInfos(anyLong(), anyString(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, favoriteGroupId)
                    .queryParam("sortBy", "최신순")
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("cursorId", String.valueOf(cursorId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR;

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
                                    "FavoritePkInfo/List/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("sortBy").description("정렬 기준"),
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("cursorId").description("데이터 위치")
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
        @DisplayName("데이터의 조회에 성공한다")
        public void successGetFavoriteResult() throws Exception {
            // given
            List<FavoritePkInfoResponse> response = new ArrayList<>();
            response.add(getInfoResponseData(favoriteInfoId));

            FavoriteQueryResponse<FavoritePkInfoResponse> responseData
                     = new FavoriteQueryResponse<>(response);

            doReturn(responseData)
                    .when(favoritePkInfoService)
                    .getFavoriteInfos(anyLong(), anyString(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, favoriteGroupId)
                    .queryParam("sortBy", "최신순")
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("cursorId", String.valueOf(cursorId));

            // then
            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.favoriteList.size()").value(1),
                            jsonPath("$.result.favoriteList[0].id").value(favoriteInfoId),
                            jsonPath("$.result.favoriteList[0].latitude").value(PARKING_LOT_1.getLatitude()),
                            jsonPath("$.result.favoriteList[0].longitude").value(PARKING_LOT_1.getLongitude()),
                            jsonPath("$.result.favoriteList[0].createAt").exists()
                    )
                    .andDo(
                            document(
                                    "FavoritePkInfo/List/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("sortBy").description("정렬 기준"),
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("cursorId").description("데이터 위치")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result.favoriteList[0].id").type(JsonFieldType.NUMBER).description("주차장 ID"),
                                            fieldWithPath("result.favoriteList[0].latitude").type(JsonFieldType.NUMBER).description("주차장 위도 좌표"),
                                            fieldWithPath("result.favoriteList[0].longitude").type(JsonFieldType.NUMBER).description("주차장 경도 좌표"),
                                            fieldWithPath("result.favoriteList[0].createAt").type(JsonFieldType.STRING).description("주차장 정보 생성 일자")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("주차장 즐겨찾기 등록 API [POST /api/favorite/info/{favoriteGroupId}]")
    class createFavoritePkInfoTest {
        private final static String BASE_URL = "/api/favorite/info/{favoriteGroupId}";
        private final static Long memberId = 1L;
        private final static Long favoriteGroupId = 1L;
        private final static Long parkingLotId = 1L;
        private final static Long errorMemberId = Long.MAX_VALUE;
        private final static Long errorFavoriteGroupId = Long.MAX_VALUE;
        private final static Long errorParkingLotId = Long.MAX_VALUE;
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, favoriteGroupId)
                    .queryParam("memberId", String.valueOf(errorMemberId))
                    .queryParam("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

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
                                    "FavoritePkInfo/Create/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
        @DisplayName("유효하지 않은 favoriteGroupId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidGroupId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, errorFavoriteGroupId)
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR;

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
                                    "FavoritePkInfo/Create/Failure/Case2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
        @DisplayName("유효하지 않은 데이터 타입인 경우, 오류를 반환한다")
        public void throwExceptionByDataType() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_TYPE_ERROR))
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, favoriteGroupId)
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_TYPE_ERROR;

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
                                    "FavoritePkInfo/Create/Failure/Case3",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
        @DisplayName("유효하지 않은 parkingLotId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLotId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, favoriteGroupId)
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("parkingLotId", String.valueOf(errorParkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR;

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
                                    "FavoritePkInfo/Create/Failure/Case4",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByExpiredData() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.INPUT_INVALID_VALUE))
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, favoriteGroupId)
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.INPUT_INVALID_VALUE;

            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "FavoritePkInfo/Create/Failure/Case5",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
        @DisplayName("주차장을 즐겨찾기에 등록하는 것에 성공한다")
        public void successCreateFavoriteInfo() throws Exception {
            // given
            doNothing()
                    .when(favoritePkInfoService)
                    .createFavoriteInfo(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, favoriteGroupId)
                    .queryParam("memberId", String.valueOf(memberId))
                    .queryParam("parkingLotId", String.valueOf(parkingLotId));

            // then
            mockMvc
                    .perform(request)
                    .andExpectAll(status().isCreated())
                    .andDo(
                            document(
                                    "FavoritePkInfo/Create/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id"),
                                            parameterWithName("parkingLotId").description("주차장 Id")
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
    @DisplayName("주차장 즐겨찾기 삭제 API [DELETE /api/favorite/info/{favoriteInfoId}]")
    class deleteFavoritePkInfoTest {
        private final static String BASE_URL = "/api/favorite/info/{favoriteInfoId}";
        private final static Long memberId = 1L;
        private final static Long errorMemberId = Long.MAX_VALUE;
        private final static Long favoriteInfoId = 1L;
        private final static Long errorFavoriteInfoId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .deleteFavoriteInfo(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteInfoId)
                    .queryParam("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

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
                              "FavoritePkInfo/Delete/Failure/Case1",
                              getDocumentRequest(),
                              getDocumentResponse(),
                              pathParameters(
                                      parameterWithName("favoriteInfoId").description("주차장 즐겨찾기 ID")
                              ),
                              queryParameters(
                                      parameterWithName("memberId").description("사용자 Id")
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
        @DisplayName("유효하지 않은 favoriteInfoId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteInfoId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR))
                    .when(favoritePkInfoService)
                    .deleteFavoriteInfo(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorFavoriteInfoId)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR;

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
                                    "FavoritePkInfo/Delete/Failure/Case2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteInfoId").description("주차장 즐겨찾기 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
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
        @DisplayName("유효하지 않은 데이터 타입인 경우, 오류를 반환한다")
        public void throwExceptionByNotMatchedData() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_TYPE_ERROR))
                    .when(favoritePkInfoService)
                    .deleteFavoriteInfo(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteInfoId)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_TYPE_ERROR;

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
                                    "FavoritePkInfo/Delete/Failure/Case3",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteInfoId").description("주차장 즐겨찾기 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
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
        @DisplayName("주차장 즐겨찾기 삭제에 성공한다")
        public void successDeleteFavoritePkInfo() throws Exception {
            // given
            doNothing()
                    .when(favoritePkInfoService)
                    .deleteFavoriteInfo(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteInfoId)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            mockMvc
                    .perform(request)
                    .andExpect(status().isNoContent())
                    .andDo(
                            document(
                                    "FavoritePkInfo/Delete/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteInfoId").description("주차장 즐겨찾기 ID")
                                    ),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
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

    private FavoritePkInfoResponse getInfoResponseData(Long favoriteInfoId) {
        return new FavoritePkInfoResponse(favoriteInfoId, PARKING_LOT_1.createEntity(), LocalDateTime.now());
    }
}