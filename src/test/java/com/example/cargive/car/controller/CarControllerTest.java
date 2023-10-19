package com.example.cargive.car.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.car.controller.dto.request.CarEditRequest;
import com.example.cargive.domain.car.controller.dto.request.CarRequest;
import com.example.cargive.domain.car.controller.dto.response.CarResponse;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cargive.car.fixture.CarFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Car [Controller Layer] -> CarController 테스트")
public class CarControllerTest extends ControllerTest {
    @Nested
    @DisplayName("사용자 차량 정보 조회 API [GET /api/car]")
    class getCarListTest {
        private final static String BASE_URL = "/api/car";
        private final static Long memberId = 1L;
        private final static Long carId = 1L;

        @Test
        @DisplayName("조회한 데이터가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_LIST_EMPTY_ERROR))
                    .when(carService)
                    .getCarList(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_LIST_EMPTY_ERROR;

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
                                    "Car/List/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
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
        @DisplayName("사용자의 차량 조회에 성공한다")
        public void successGetCarList() throws Exception {
            // given
            List<CarResponse> responseData = getCarResponseList(carId);

            doReturn(responseData)
                    .when(carService)
                    .getCarList(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            mockMvc.perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.size()").value(1))
                    .andDo(
                            document(
                                    "Car/List/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result[0].id").type(JsonFieldType.NUMBER).description("차량 Id"),
                                            fieldWithPath("result[0].type").type(JsonFieldType.STRING).description("차량 종류"),
                                            fieldWithPath("result[0].number").type(JsonFieldType.STRING).description("차량 번호"),
                                            fieldWithPath("result[0].recentCheck").type(JsonFieldType.STRING).description("마지막 점검 일자"),
                                            fieldWithPath("result[0].mileage").type(JsonFieldType.NUMBER).description("주행 거리"),
                                            fieldWithPath("result[0].imageUrl").type(JsonFieldType.STRING).description("차량 이미지 접근 URL"),
                                            fieldWithPath("result[0].tagList").type(JsonFieldType.ARRAY).description("차량 특징 카드 목록"),
                                            fieldWithPath("result[0].tagList[0].id").type(JsonFieldType.NUMBER).description("차량 특징 카드 Id"),
                                            fieldWithPath("result[0].tagList[0].name").type(JsonFieldType.STRING).description("차량 특징 카드 이름"),
                                            fieldWithPath("result[0].favorite").type(JsonFieldType.BOOLEAN).description("즐겨찾기 여부")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("차량 생성 API [POST /api/car]")
    class createCarTest {
        private final static String BASE_URL = "/api/car";
        private final static Long memberId = 1L;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(carService)
                    .createCar(any(), any(), anyLong());

            CarRequest carRequest = getCarCreateRequest();

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carRequest).getBytes(StandardCharsets.UTF_8));

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(errorMemberId))
                    .accept(MediaType.APPLICATION_JSON);

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
                                    "Car/Create/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
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
        @DisplayName("차량 생성에 성공한다")
        public void successCreateCar() throws Exception {
            // given
            doNothing()
                    .when(carService)
                    .createCar(any(), any(), anyLong());

            CarRequest carRequest = getCarCreateRequest();

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carRequest).getBytes(StandardCharsets.UTF_8));

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(memberId))
                    .accept(MediaType.APPLICATION_JSON);

            // then
            mockMvc.perform(request)
                    .andExpectAll(
                            status().isCreated()
                    ).andDo(
                            document(
                                    "Car/Create/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
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

    @Nested
    @DisplayName("차량 수정 API [PUT /api/car/{carId}]")
    class editCarTest {
        private final static String BASE_URL = "/api/car/{carId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            CarEditRequest carEditRequest = getCarEditRequest();

            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(carService)
                    .editCar(any(), any(), anyLong(), anyLong());

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carEditRequest).getBytes(StandardCharsets.UTF_8));

            // when
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_NOT_FOUND_ERROR;
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL, errorCarId)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(memberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then
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
                                    "Car/Edit/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            CarEditRequest carEditRequest = getCarEditRequest();

            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(carService)
                    .editCar(any(), any(), anyLong(), anyLong());

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carEditRequest).getBytes(StandardCharsets.UTF_8));

            // when
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_NOT_FOUND_ERROR;
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL, carId)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(errorMemberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then
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
                                    "Car/Edit/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("등록된 사용자의 정보와 이용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            CarEditRequest carEditRequest = getCarEditRequest();

            doThrow(new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR))
                    .when(carService)
                    .editCar(any(), any(), anyLong(), anyLong());

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carEditRequest).getBytes(StandardCharsets.UTF_8));

            // when
            final BaseResponseStatus expectException = BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR;
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL, carId)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(memberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then
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
                                    "Car/Edit/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("차량 정보 수정에 성공한다")
        public void successEditCar() throws Exception {
            // given
            CarEditRequest carEditRequest = getCarEditRequest();

            doNothing()
                    .when(carService)
                    .editCar(any(), any(), anyLong(), anyLong());

            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(carEditRequest).getBytes(StandardCharsets.UTF_8));

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL, carId)
                    .file(file)
                    .file(mockRequest)
                    .queryParam("memberId", String.valueOf(memberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then

            mockMvc.perform(request)
                    .andExpectAll(
                            status().isOk()
                    ).andDo(
                            document(
                                    "Car/Edit/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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

    @Nested
    @DisplayName("차량 삭제 API [DELETE /api/car/{carId}]")
    class deleteCarTest {
        private final static String BASE_URL = "/api/car/{carId}";
        private final static Long carId = 1L;
        private final static Long memberId = 1L;
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR))
                    .when(carService)
                    .deleteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, errorCarId)
                    .queryParam("memberId", String.valueOf(memberId));

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
                                    "Car/Delete/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception{
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(carService)
                    .deleteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, carId)
                    .queryParam("memberId", String.valueOf(errorMemberId));

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
                                    "Car/Delete/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("등록된 사용자의 정보와 이용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR))
                    .when(carService)
                    .deleteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, carId)
                    .queryParam("memberId", String.valueOf(memberId));

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
                                    "Car/Delete/Failure/Case3",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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
        @DisplayName("차량 정보 삭제에 성공한다")
        public void successDeleteCar() throws Exception {
            // given
            doNothing()
                    .when(carService)
                    .deleteCar(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL, carId)
                    .queryParam("memberId", String.valueOf(memberId));

            // then
            mockMvc
                    .perform(request)
                    .andExpect(status().isNoContent())
                    .andDo(
                            document(
                                    "Car/Delete/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    pathParameters(
                                            parameterWithName("carId").description("차량 Id")
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

    private CarRequest getCarCreateRequest() {
        CarRequest carRequest = new CarRequest();
        carRequest.setType("TestType");
        carRequest.setNumber("9999");
        carRequest.setMileage(1000L);
        carRequest.setRecentCheck(LocalDate.now());
        carRequest.setTagList(Arrays.asList("Test1", "Test2", "Test3"));
        return carRequest;
    }

    private CarEditRequest getCarEditRequest() {
        CarEditRequest request = new CarEditRequest();
        request.setMileage(1000L);
        request.setRecentCheck(LocalDate.now());
        request.setAddedTagList(Arrays.asList("Tag1", "Tag2", "Tag3"));
        request.setDeletedTagList(Arrays.asList(1L, 2L, 3L));

        return request;
    }

    private List<CarResponse> getCarResponseList(Long carId) {
        List<CarResponse> responseList = new ArrayList<>();
        List<TagResponse> tagList = new ArrayList<>();
        tagList.add(new TagResponse(1L, "Test1"));

        responseList.add(new CarResponse(carId, CAR_1.getType(), CAR_1.getNumber(), CAR_1.getRecentCheck(),
                CAR_1.getMileage(), CAR_1.getImageUrl(), CAR_1.isFavorite(), tagList));

        return responseList;
    }
}