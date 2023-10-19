package com.example.cargive.parkinglot.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.parkingLot.controller.dto.request.ParkingLotRequest;
import com.example.cargive.domain.parkingLot.controller.dto.response.ParkingLotResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ParkingLot [Controller Layer] -> ParkingLotController 테스트")
public class ParkingLotControllerTest extends ControllerTest {
    @Nested
    @DisplayName("ParkingLot 도메인 단일 조회 API [GET /api/parkinglot/{parkingLotId}]")
    class selectParkingLotTest {
        private static final String BASE_URL = "/api/parkinglot/{parkingLotId}";
        private static final Long parkingLotId = 1L;
        private static final Long errorParkingLotId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 ParkingLot Entity의 PK값으로 요청한 경우, 오류를 반환한다")
        public void throwExceptionByInvalidId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR))
                    .when(parkingLotService)
                    .findParkingLot(errorParkingLotId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, errorParkingLotId);

            // then

            final BaseResponseStatus expectException = BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR;

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
                                    "ParkingLot/Info/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("parkingLotId").description("주차장 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("ParkingLot 도메인 데이터를 조회한다")
        public void successSelectParkingLot() throws Exception {
            // given
            ParkingLotResponse responseData = getResponseData(parkingLotId);

            doReturn(responseData)
                    .when(parkingLotService)
                    .findParkingLot(parkingLotId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL, parkingLotId);

            // then
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(
                            document("ParkingLot/Info/Success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(parameterWithName("parkingLotId").description("주차장 ID")),
                            responseFields(
                                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("주차장 Entity Id"),
                                    fieldWithPath("result.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                    fieldWithPath("result.longitude").type(JsonFieldType.NUMBER).description("경도")
                            )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("ParkingLot 도메인 생성 API [POST /api/parkinglot]")
    class createParkingLotTest {
        private final static String BASE_URL = "/api/parkinglot";
        @Test
        @DisplayName("ParkingLot 도메인을 생성한다")
        public void successCreateParkingLot() throws Exception {
            // given
            ParkingLotRequest requestData = getRequestData();

            doNothing()
                    .when(parkingLotService)
                    .createParkingLot(requestData);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestData));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isCreated())
                    .andDo(
                            document(
                                    "ParkingLot/Save/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    requestFields(
                                            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("주차장 경도 좌표"),
                                            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("주차장 위도 좌표")
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
    @DisplayName("ParkingLot 도메인 삭제 API [DELETE /api/parkinglot/{parkingLotId}]")
    class deleteParkingLotTest {
        private final static String BASE_URL = "/api/parkinglot/{parkingLotId}";
        private final static Long parkingLotId = 1L;
        private final static Long errorParkingLotId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 ParkingLot Entity의 PK값으로 요청한 경우, 오류를 반환한다")
        public void throwExceptionByInvalidId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR))
                    .when(parkingLotService)
                    .deleteParkingLot(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorParkingLotId);

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
                            jsonPath("$.message").value(expectException.getMessage()))
                    .andDo(
                            document(
                                    "ParkingLot/DELETE/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("parkingLotId").description("주차장 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("ParkingLot 도메인을 삭제한다")
        public void successDeleteParkingLot() throws Exception {
            // given
            doNothing()
                    .when(parkingLotService)
                    .deleteParkingLot(parkingLotId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, parkingLotId);

            // then
            mockMvc.perform(request)
                    .andExpect(status().isNoContent())
                    .andDo(
                            document(
                                    "ParkingLot/DELETE/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(parameterWithName("parkingLotId").description("주차장 ID")),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
                                    )
                            )
                    );
        }
    }

    private ParkingLotRequest getRequestData() {
        return new ParkingLotRequest(PARKING_LOT_1.getLatitude(), PARKING_LOT_1.getLongitude());
    }

    private ParkingLotResponse getResponseData(Long parkingLotId) {
        return new ParkingLotResponse(parkingLotId, PARKING_LOT_1.getLatitude(), PARKING_LOT_1.getLongitude());
    }
}