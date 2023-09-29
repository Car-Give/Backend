package com.example.cargive.favorite.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupEditRequest;
import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupRequest;
import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.favorite.service.FavoritePkGroupService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("FavoritePkGroup [Service Layer] -> FavoritePkGroupService 테스트")
public class FavoritePkGroupServiceTest extends ServiceTest {
    @Autowired
    private FavoritePkGroupService favoritePkGroupService;
    private FavoritePkGroup favoritePkGroup;
    private Member member;
    private Member otherMember;
    private Long favoriteGroupId;
    private Long memberId;
    private Long otherMemberId;
    private Long cursorId;
    private Long errorFavoriteGroupId = Long.MAX_VALUE;
    private Long errorMemberId = Long.MAX_VALUE;
    private String sortCondition = "최신순";

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        otherMember = ASSAC.toMember();
        favoritePkGroup = new FavoritePkGroup("TestName", member);

        memberId = memberRepository.save(member).getId();
        otherMemberId = memberRepository.save(otherMember).getId();
        favoriteGroupId = favoriteRepository.save(favoritePkGroup).getId();
        cursorId = 10L;
    }

    @AfterEach
    public void afterTest() {
        favoriteRepository.deleteAll();
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 조회")
    class findFavoriteGroupsTest {
        @Test
        @DisplayName("유효하지 않은 정렬 조건이 주어졌을 경우, 오류를 반환한다")
        public void throwExceptionByUnsupportedCondition() {
            // when - then
            assertThatThrownBy(() ->
                            favoritePkGroupService.findFavoriteGroups(memberId, "InValid", cursorId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회 결과가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.findFavoriteGroups(memberId, sortCondition, 0L))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successFindFavoriteGroups() {
            // when
            FavoriteQueryResponse<FavoritePkGroupResponse> response =
                    favoritePkGroupService.findFavoriteGroups(memberId, sortCondition, cursorId);

            // then
            assertThat(response.getFavoriteList().size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 생성")
    class createFavoriteGroupTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.createFavoriteGroup(errorMemberId, getFavoriteGroupRequest()))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("즐겨찾기 그룹 생성에 성공한다")
        public void successCreateFavoriteGroup() {
            // when
            favoritePkGroupService.createFavoriteGroup(memberId, getFavoriteGroupRequest());

            // then
            assertThat(favoriteRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 수정")
    class editFavoriteGroupTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.editFavoriteGroup(errorMemberId, favoriteGroupId, getFavoriteGroupEditRequest()))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 favoriteGroupId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidGroupId() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.editFavoriteGroup(memberId, errorFavoriteGroupId, getFavoriteGroupEditRequest()))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터에 저장된 정보와 이용자의 정보가 일치하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.editFavoriteGroup(otherMemberId, favoriteGroupId, getFavoriteGroupEditRequest()))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("즐겨찾기 그룹 수정에 성공한다")
        public void successEditFavoriteGroup() {
            // when
            favoritePkGroupService.editFavoriteGroup(memberId, favoriteGroupId, getFavoriteGroupEditRequest());

            // then
            assertThat(favoritePkGroup.getName()).isEqualTo("EditName");
        }
    }

    @Nested
    @DisplayName("즐겨찾기 그룹 삭제")
    class deleteFavoriteGroupTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.deleteFavoriteGroup(errorMemberId, favoriteGroupId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 favoriteGroupId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidGroupId() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.deleteFavoriteGroup(memberId, errorFavoriteGroupId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터에 저장된 정보와 이용자의 정보가 일치하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> favoritePkGroupService.deleteFavoriteGroup(otherMemberId, favoriteGroupId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("즐겨찾기 그룹 삭제에 성공한다")
        public void successDeleteFavoriteGroup() {
            // when
            favoritePkGroupService.deleteFavoriteGroup(memberId, favoriteGroupId);

            // then
            assertThat(favoritePkGroup.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private FavoriteGroupRequest getFavoriteGroupRequest() {
        return new FavoriteGroupRequest("NewName");
    }

    private FavoriteGroupEditRequest getFavoriteGroupEditRequest() {
        return new FavoriteGroupEditRequest("EditName");
    }
}