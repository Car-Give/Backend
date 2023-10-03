package com.example.cargive.history.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.history.controller.dto.HistoryResponse;
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

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("History [Controller Layer] -> HistoryController 테스트")
public class HistoryControllerTest extends ControllerTest {
    @Nested
    @DisplayName("차량 이용 기록 조회 API [GET /api/history/{carId}]")
    class getHistoryListTest {
        private final static String BASE_URL = "/api/history/{carId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("조회한 결과가 존재하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.HISTORY_LIST_EMPTY_ERROR))
                    .when(historyService)
                    .getHistoryList(errorCarId, errorMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, errorCarId)
                    .param("memberId", String.valueOf(errorMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.HISTORY_LIST_EMPTY_ERROR;

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
                                    "History/List/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
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
        public void successGetHistoryList() throws Exception {
            // given
            List<HistoryResponse> responseList = getResponseList();

            doReturn(responseList)
                    .when(historyService)
                    .getHistoryList(carId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.size()").value(1),
                            jsonPath("$.result[0].createAt").exists(),
                            jsonPath("$.result[0].updateAt").exists(),
                            jsonPath("$.result[0].status").isBoolean()
                    ).andDo(
                            document(
                                    "History/List/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result[0].id").type(JsonFieldType.NUMBER).description("History Id"),
                                            fieldWithPath("result[0].createAt").type(JsonFieldType.STRING).description("생성 일자"),
                                            fieldWithPath("result[0].updateAt").type(JsonFieldType.STRING).description("수정 일자"),
                                            fieldWithPath("result[0].status").type(JsonFieldType.BOOLEAN).description("종료 여부")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("이용 시작 API [POST /api/history/{carId}]")
    class startUsingTest {
        private final static String BASE_URL = "/api/history/{carId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long parkingLotId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;
        private final static Long errorParkingLotId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(historyService)
                    .startUseHistory(carId, parkingLotId, errorMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(errorMemberId))
                    .param("parkingLotId", String.valueOf(parkingLotId));

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
                                    "History/Create/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
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
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(historyService)
                    .startUseHistory(errorCarId, parkingLotId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, errorCarId)
                    .param("memberId", String.valueOf(memberId))
                    .param("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_FOUND_ERROR;

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
                                    "History/Create/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
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
        public void throwExceptionByInvalidParkingLotId() throws Exception{
            // given
            doThrow(new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR))
                    .when(historyService)
                    .startUseHistory(carId, errorParkingLotId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId))
                    .param("parkingLotId", String.valueOf(errorParkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR;

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
                                    "History/Create/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
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
        @DisplayName("저장된 데이터와 이용자의 정보가 일치하지 않을경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR))
                    .when(historyService)
                    .startUseHistory(carId, parkingLotId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId))
                    .param("parkingLotId", String.valueOf(parkingLotId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "History/Create/Failure/Case4",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
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
        @DisplayName("이용 시작에 성공한다")
        public void successStartUsing() throws Exception {
            // given
            doNothing()
                    .when(historyService)
                    .startUseHistory(carId, parkingLotId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL, carId)
                    .param("memberId", String.valueOf(memberId))
                    .param("parkingLotId", String.valueOf(parkingLotId));

            // then
            mockMvc.perform(request)
                    .andExpect(
                            status().isCreated()
                    ).andDo(
                            document(
                                    "History/Create/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 ID")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("이용 종료 API [PUT /api/history/{historyId}]")
    class endUsingTest {
        private final static String BASE_URL = "/api/history/{historyId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long historyId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;
        private final static Long errorHistoryId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(historyService)
                    .endUseHistory(historyId, errorCarId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, historyId)
                    .param("carId", String.valueOf(errorCarId))
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_FOUND_ERROR;

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
                                    "History/End/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
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
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(historyService)
                    .endUseHistory(historyId, carId, errorMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, historyId)
                    .param("carId", String.valueOf(carId))
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
                                    "History/End/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
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
        @DisplayName("유효하지 않은 historyId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidHistoryId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.HISTORY_NOT_FOUND_ERROR))
                    .when(historyService)
                    .endUseHistory(errorHistoryId, carId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, errorHistoryId)
                    .param("carId", String.valueOf(carId))
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.HISTORY_NOT_FOUND_ERROR;

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
                                    "History/End/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
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
        @DisplayName("저장된 데이터와 사용자의 정보가 일치하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR))
                    .when(historyService)
                    .endUseHistory(historyId, carId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, historyId)
                    .param("carId", String.valueOf(carId))
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "History/End/Failure/Case4",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
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
        @DisplayName("저장된 데이터와 차량의 정보가 일치하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCar() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.HISTORY_CAR_NOT_MATCH_ERROR))
                    .when(historyService)
                    .endUseHistory(historyId, carId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, historyId)
                    .param("carId", String.valueOf(carId))
                    .param("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.HISTORY_CAR_NOT_MATCH_ERROR;

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectException.getStatus().value()),
                            jsonPath("$.code").exists(),
                            jsonPath("$.code").value(expectException.getCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectException.getMessage())
                    ).andDo(
                            document(
                                    "History/End/Failure/Case5",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
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
        @DisplayName("이용 종료에 성공한다")
        public void successEndUsing() throws Exception {
            // given
            doNothing()
                    .when(historyService)
                    .endUseHistory(historyId, carId, memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .put(BASE_URL, historyId)
                    .param("carId", String.valueOf(carId))
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "History/End/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("historyId").description("이용 기록 ID")
                                    )
                            )
                    );
        }
    }

    private List<HistoryResponse> getResponseList() {
        List<HistoryResponse> responseList = new ArrayList<>();
        responseList.add(new HistoryResponse(1L, LocalDateTime.now(), LocalDateTime.now(), true));

        return responseList;
    }
}