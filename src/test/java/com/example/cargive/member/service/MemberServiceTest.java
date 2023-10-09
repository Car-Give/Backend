package com.example.cargive.member.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.member.controller.dto.request.MemberRequest;
import com.example.cargive.domain.member.controller.dto.response.MemberResponse;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.service.MemberService;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member [Service Layer] -> MemberService 테스트")
public class MemberServiceTest extends ServiceTest {
    @Autowired
    private MemberService memberService;
    private Member member;
    private Member deleteMember;
    private Long memberId;
    private Long deleteMemberId;
    private Long errorMemberId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        deleteMember = ASSAC.toMember();

        deleteMember.deleteEntity();

        memberId = memberRepository.save(member).getId();
        deleteMemberId = memberRepository.save(deleteMember).getId();
    }

    @AfterEach
    public void afterTest() {
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("사용자 정보 조회")
    public class getMemberInfoTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> memberService.getMemberInfo(errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> memberService.getMemberInfo(deleteMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetMemberInfo() {
            // when
            MemberResponse memberResponse = memberService.getMemberInfo(memberId);

            // then
            assertAll(
                    () -> assertThat(memberResponse.name()).isEqualTo(member.getName()),
                    () -> assertThat(memberResponse.imageUrl()).isEqualTo(member.getImageUrl()),
                    () -> assertThat(memberResponse.phoneNumber()).isEqualTo(member.getPhoneNumber()),
                    () -> assertThat(memberResponse.loginId()).isEqualTo(member.getLoginId()),
                    () -> assertThat(memberResponse.email()).isEqualTo(member.getEmail()),
                    () -> assertThat(memberResponse.social()).isEqualTo(member.getSocial())
            );
        }
    }

    @Nested
    @DisplayName("사용자 정보 수정")
    public class editMemberTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // given
            MemberRequest request = getMemberRequest();
            MockMultipartFile file = new MockMultipartFile("image", "test",
                    "", "test".getBytes());

            // when - then
            assertThatThrownBy(() -> memberService.editMember(errorMemberId, request, file))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // given
            MemberRequest request = getMemberRequest();
            MockMultipartFile file = new MockMultipartFile("image", "test",
                    "", "test".getBytes());

            // when - then
            assertThatThrownBy(() -> memberService.editMember(deleteMemberId, request, file))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 수정에 성공한다")
        public void successEditMemberInfo() {
            // given
            MemberRequest request = getMemberRequest();
            MockMultipartFile file = new MockMultipartFile("image", "test",
                    "", "test".getBytes());

            // when
            memberService.editMember(memberId, request, file);

            // then
            assertAll(
                    () -> assertThat(member.getPhoneNumber()).isEqualTo(request.phoneNumber()),
                    () -> assertThat(member.getImageUrl()).isNotEqualTo(WIZ.getImageUrl())
            );
        }
    }

    @Nested
    @DisplayName("사용자 정보 삭제")
    public class deleteMemberTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> memberService.deleteMember(errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("이미 삭제된 데이터인 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> memberService.deleteMember(deleteMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 삭제에 성공한다")
        public void successDeleteMemberInfo() {
            // when
            memberService.deleteMember(memberId);

            // then
            assertThat(member.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private MemberRequest getMemberRequest() {
        return new MemberRequest("010-1111-2222");
    }
}
