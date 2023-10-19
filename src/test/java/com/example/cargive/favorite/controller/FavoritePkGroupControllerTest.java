package com.example.cargive.favorite.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupRequest;
import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
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
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("FavoritePkGroup [Controller Layer] -> FavoritePkGroupController 테스트")
public class FavoritePkGroupControllerTest extends ControllerTest {
    @Nested
    @DisplayName("즐겨찾기 그룹 조회 API [GET /api/favorite/group]")
    class getGroupListTest {
        private final static String BASE_URL = "/api/favorite/group";
        private final static Long memberId = 1L;
        private final static Long cursorId = 1L;
        private final static Long favoriteGroupId = 1L;
        private final static String sortBy = "정렬기준";

        @Test
        @DisplayName("데이터 조회 결과가 존재하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR))
                    .when(favoritePkGroupService)
                    .findFavoriteGroups(anyLong(), anyString(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("sortBy", sortBy)
                    .param("memberId", String.valueOf(memberId))
                    .param("cursorId", String.valueOf(cursorId));

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
                                    "FavoritePkGroup/List/Failure/Case1",
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
        @DisplayName("지원하지 않는 정렬 기준으로 요청하는 경우, 오류를 반환다.")
        public void throwExceptionByInvalidSortCondition() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.NOT_FOUND_SORT_CONDITION))
                    .when(favoritePkGroupService)
                    .findFavoriteGroups(anyLong(), anyString(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("sortBy", sortBy)
                    .param("memberId", String.valueOf(memberId))
                    .param("cursorId", String.valueOf(cursorId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.NOT_FOUND_SORT_CONDITION;

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
                                    "FavoritePkGroup/List/Failure/Case2",
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
        @DisplayName("즐겨찾기 그룹 조회에 성공한다")
        public void successFindFavoriteGroup() throws Exception {
            // given
            List<FavoritePkGroupResponse> responseList = getGroupResponseList(favoriteGroupId);
            FavoriteQueryResponse<FavoritePkGroupResponse> responseData =
                    new FavoriteQueryResponse<>(responseList);

            doReturn(responseData)
                    .when(favoritePkGroupService)
                    .findFavoriteGroups(anyLong(), anyString(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("sortBy", sortBy)
                    .param("memberId", String.valueOf(memberId))
                    .param("cursorId", String.valueOf(cursorId));

            // then
            mockMvc.perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.favoriteList.size()").value(2),
                            jsonPath("$.result.favoriteList[0]").exists(),
                            jsonPath("$.result.favoriteList[1]").exists()
                    ).andDo(
                            document(
                                    "FavoritePkGroup/List/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result.favoriteList[0].id").type(JsonFieldType.NUMBER).description("그룹 ID"),
                                            fieldWithPath("result.favoriteList[0].name").type(JsonFieldType.STRING).description("그룹 이름"),
                                            fieldWithPath("result.favoriteList[0].createAt").type(JsonFieldType.STRING).description("즐겨찾기 그룹 생성 일자"),
                                            fieldWithPath("result.favoriteList[1].id").type(JsonFieldType.NUMBER).description("그룹 ID"),
                                            fieldWithPath("result.favoriteList[1].name").type(JsonFieldType.STRING).description("그룹 이름"),
                                            fieldWithPath("result.favoriteList[1].createAt").type(JsonFieldType.STRING).description("즐겨찾기 그룹 생성 일자")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 생성 API [POST /api/favorite/group]")
    class createGroupTest {
        private final static String BASE_URL = "/api/favorite/group";
        private final static Long memberId = 1L;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoritePkGroupService)
                    .createFavoriteGroup(anyLong(), any());

            FavoriteGroupRequest createRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .content(objectMapper.writeValueAsString(createRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

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
                                    "FavoritePkGroup/Create/Failure/Case1",
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
        @DisplayName("즐겨찾기 그룹 생성에 성공한다")
        public void successCreateGroup() throws Exception {
            // given
            doNothing()
                    .when(favoritePkGroupService)
                    .createFavoriteGroup(anyLong(), any());

            FavoriteGroupRequest createRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .content(objectMapper.writeValueAsString(createRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isCreated())
                    .andDo(
                            document(
                                    "FavoritePkGroup/Create/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse()
                            )
                    );
        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 수정 API [PUT /api/favorite/group/{favoriteGroupId}]")
    class editGroupTest {
        private static final String BASE_URL = "/api/favorite/group/{favoriteGroupId}";
        private static final Long favoriteGroupId = 1L;
        private static final Long memberId = 1L;
        private static final Long errorFavoriteGroupId = Long.MAX_VALUE;
        private static final Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoritePkGroupService)
                    .editFavoriteGroup(anyLong(), anyLong(), any());

            FavoriteGroupRequest groupEditRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(errorMemberId))
                    .content(objectMapper.writeValueAsString(groupEditRequest))
                    .contentType(MediaType.APPLICATION_JSON);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

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
                                    "FavoritePkGroup/Edit/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 즐겨찾기 그룹의 이름")
                                    ),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
                    .when(favoritePkGroupService)
                    .editFavoriteGroup(anyLong(), anyLong(), any());

            FavoriteGroupRequest groupEditRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, errorFavoriteGroupId)
                    .param("memberId", String.valueOf(memberId))
                    .content(objectMapper.writeValueAsString(groupEditRequest))
                    .contentType(MediaType.APPLICATION_JSON);

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
                                    "FavoritePkGroup/Edit/Failure/Case2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 즐겨찾기 그룹의 이름")
                                    ),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
        @DisplayName("등록된 사용자의 정보와 이용자의 정보가 일치하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR))
                    .when(favoritePkGroupService)
                    .editFavoriteGroup(anyLong(), anyLong(), any());

            FavoriteGroupRequest groupEditRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(memberId))
                    .content(objectMapper.writeValueAsString(groupEditRequest))
                    .contentType(MediaType.APPLICATION_JSON);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
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
                                    "FavoritePkGroup/Edit/Failure/Case3",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 즐겨찾기 그룹의 이름")
                                    ),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
        @DisplayName("즐겨찾기 그룹 수정에 성공한다")
        public void successEditGroup() throws Exception {
            // given
            doNothing()
                    .when(favoritePkGroupService)
                    .editFavoriteGroup(anyLong(), anyLong(), any());

            FavoriteGroupRequest editRequest = getGroupRequest();

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(memberId))
                    .content(objectMapper.writeValueAsString(editRequest))
                    .contentType(MediaType.APPLICATION_JSON);

            // then
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "FavoritePkGroup/Edit/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 즐겨찾기 그룹의 이름")
                                    ),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
                                    )
                            )
                    );

        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 삭제 API [DELETE /api/favorite/group/{favoriteGroupId}")
    class deleteGroupTest {
        private final static String BASE_URL = "/api/favorite/group/{favoriteGroupId}";
        private static final Long favoriteGroupId = 1L;
        private static final Long memberId = 1L;
        private static final Long errorFavoriteGroupId = Long.MAX_VALUE;
        private static final Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(favoritePkGroupService)
                    .deleteFavoriteGroup(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;

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
                                    "FavoritePkGroup/Delete/Failure/Case1",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
                    .when(favoritePkGroupService)
                    .deleteFavoriteGroup(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorFavoriteGroupId)
                    .param("memberId", String.valueOf(memberId));

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
                                    "FavoritePkGroup/Delete/Failure/Case2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
        @DisplayName("등록된 사용자의 정보와 이용자의 정보가 일치하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR))
                    .when(favoritePkGroupService)
                    .deleteFavoriteGroup(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
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
                                    "FavoritePkGroup/Delete/Failure/Case3",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
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
        @DisplayName("즐겨찾기 그룹 삭제에 성공한다")
        public void successDeleteGroup() throws Exception {
            // given
            doNothing()
                    .when(favoritePkGroupService)
                    .deleteFavoriteGroup(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, favoriteGroupId)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isNoContent())
                    .andDo(
                            document(
                                    "FavoritePkGroup/Delete/Success",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("favoriteGroupId").description("즐겨찾기 그룹 Id")
                                    )
                            )
                    );
        }
    }

    private List<FavoritePkGroupResponse> getGroupResponseList(long favoriteGroupId) {
        List<FavoritePkGroupResponse> responseList = new ArrayList<>();
        responseList.add(new FavoritePkGroupResponse(favoriteGroupId, "Test Group1", LocalDateTime.now()));
        responseList.add(new FavoritePkGroupResponse(favoriteGroupId + 1, "Test Group2", LocalDateTime.now().plusHours(1)));

        return responseList;
    }

    private FavoriteGroupRequest getGroupRequest() {
        return new FavoriteGroupRequest("Test Group Name");
    }
}
