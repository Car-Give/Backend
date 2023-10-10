package com.example.cargive.member.controller;

import com.example.cargive.common.ControllerTest;
import com.example.cargive.domain.member.controller.dto.request.MemberRequest;
import com.example.cargive.domain.member.controller.dto.response.MemberResponse;
import com.example.cargive.domain.member.entity.Member;
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

import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Member [Controller Layer] -> MemberController 테스트")
public class MemberControllerTest extends ControllerTest {
    @Nested
    @DisplayName("Member 도메인 단일 조회 API [GET /api/member]")
    public class getMemberTest {
        private final String BASE_URL = "/api/member";
        private final Long memberId = 1L;
        private final Long deleteMemberId = 12345L;
        private final Long errorMemberId = Long.MAX_VALUE;
        private final Member member = WIZ.toMember();
        private final MemberResponse memberResponse = MemberResponse.toDto(member);

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(memberService)
                    .getMemberInfo(errorMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("memberId", String.valueOf(errorMemberId));

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
                                    "Member/Info/Failure/Case1",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR))
                    .when(memberService)
                    .getMemberInfo(deleteMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("memberId", String.valueOf(deleteMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR;

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
                                    "Member/Info/Failure/Case2",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"))
                            )
                    );
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetMember() throws Exception {
            // given
            doReturn(memberResponse)
                    .when(memberService)
                    .getMemberInfo(memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc
                    .perform(request)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.result.loginId").value(memberResponse.loginId()),
                            jsonPath("$.result.name").value(memberResponse.name()),
                            jsonPath("$.result.phoneNumber").value(memberResponse.phoneNumber()),
                            jsonPath("$.result.email.value").value(memberResponse.email().getValue()),
                            jsonPath("$.result.social").value(memberResponse.social().toString()),
                            jsonPath("$.result.imageUrl").value(memberResponse.imageUrl())

                    ).andDo(
                            document(
                                    "Member/Info/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    queryParameters(
                                            parameterWithName("memberId").description("사용자 Id")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                            fieldWithPath("code").type(JsonFieldType.STRING).description("커스텀 상태 코드"),
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                                            fieldWithPath("result.loginId").type(JsonFieldType.STRING).description("사용자 아이디"),
                                            fieldWithPath("result.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                            fieldWithPath("result.phoneNumber").type(JsonFieldType.STRING).description("사용자 핸드폰 번호"),
                                            fieldWithPath("result.email.value").type(JsonFieldType.STRING).description("사용자 이메일"),
                                            fieldWithPath("result.social").type(JsonFieldType.STRING).description("사용자 소셜 계정"),
                                            fieldWithPath("result.imageUrl").type(JsonFieldType.NULL).description("사용자 프로필 이미지 URL"))
                            )
                    );
        }
    }

    @Nested
    @DisplayName("Member 도메인 수정 API [PUT /api/member]")
    public class editMemberTest {
        private final String BASE_URL = "/api/member";
        private final Long memberId = 1L;
        private final Long errorMemberId = Long.MAX_VALUE;
        private final MemberRequest memberEditRequest = new MemberRequest(ASSAC.getPhoneNumber());

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(memberEditRequest).getBytes(StandardCharsets.UTF_8));

            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(memberService)
                    .editMember(any(), any(), any());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL)
                    .file(file)
                    .file(mockRequest)
                    .param("memberId", String.valueOf(errorMemberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

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
                                    "Member/Edit/Failure/Case1",
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
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(memberEditRequest).getBytes(StandardCharsets.UTF_8));

            doThrow(new BaseException(BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR))
                    .when(memberService)
                    .editMember(any(), any(), any());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL)
                    .file(file)
                    .file(mockRequest)
                    .param("memberId", String.valueOf(errorMemberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR;

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
                                    "Member/Edit/Failure/Case2",
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
        @DisplayName("데이터 수정에 성공한다")
        public void successEditMember() throws Exception {
            // given
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", objectMapper.writeValueAsString(memberEditRequest).getBytes(StandardCharsets.UTF_8));

            doNothing()
                    .when(memberService)
                    .editMember(any(), any(), any());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .multipart(BASE_URL)
                    .file(file)
                    .file(mockRequest)
                    .param("memberId", String.valueOf(memberId))
                    .with(req -> {
                        req.setMethod("PUT");
                        return req;
                    })
                    .accept(MediaType.APPLICATION_JSON);

            // then
            mockMvc
                    .perform(request)
                    .andExpect(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "Member/Edit/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()))
                    );
        }
    }

    @Nested
    @DisplayName("Member 도메인 삭제 [DELETE /api/member]")
    public class deleteMemberTest {
        private final String BASE_URL = "/api/member";
        private final Long memberId = 1L;
        private final Long deleteMemberId = memberId + 1;
        private final Long errorMemberId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR))
                    .when(memberService)
                    .deleteMember(errorMemberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL)
                    .param("memberId", String.valueOf(errorMemberId));

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
                                    "Member/Delete/Failure/Case1",
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
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() throws Exception {
            // given
            doThrow(new BaseException(BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR))
                    .when(memberService)
                    .deleteMember(anyLong());

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL)
                    .param("memberId", String.valueOf(deleteMemberId));

            // then
            final BaseResponseStatus expectException = BaseResponseStatus.MEMBER_STATUS_NOT_VALID_ERROR;

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
                                    "Member/Delete/Failure/Case2",
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
        @DisplayName("데이터 삭제에 성공한다")
        public void successDeleteMember() throws Exception {
            // given
            doNothing()
                    .when(memberService)
                    .deleteMember(memberId);

            // when
            MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
                    .delete(BASE_URL)
                    .param("memberId", String.valueOf(memberId));

            // then
            mockMvc
                    .perform(request)
                    .andExpect(
                            status().isNoContent()
                    ).andDo(
                            document(
                                    "Member/Delete/Success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint())
                            )
                    );
        }
    }
}
