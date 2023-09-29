package com.example.cargive.favorite.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.entity.FavoritePkInfo;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.favorite.service.FavoritePkInfoService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.base.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.member.fixture.MemberFixture.*;
import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("FavoritePkInfo [Service Layer] -> FavoritePkInfoService 테스트")
public class FavoritePkInfoServiceTest extends ServiceTest {
    @Autowired
    private FavoritePkInfoService favoritePkInfoService;

    private Member member;
    private Member otherMember;
    private FavoritePkGroup favoritePkGroup;
    private ParkingLot parkingLot;
    private FavoritePkInfo favoritePkInfo;
    private Long memberId;
    private Long otherMemberId;
    private Long errorMemberId = Long.MAX_VALUE;
    private Long favoriteGroupId;
    private Long errorFavoriteGroupId = Long.MAX_VALUE;
    private Long otherFavoriteGroupId;
    private Long favoritePkInfoId;
    private Long errorFavoriteInfoId = Long.MAX_VALUE;
    private Long parkingLotId;
    private Long errorParkingLotId = Long.MAX_VALUE;
    private Long cursorId = 10L;
    private String sortCondition = "최신순";

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        otherMember = ASSAC.toMember();
        parkingLot = PARKING_LOT_1.createEntity();
        favoritePkGroup = new FavoritePkGroup("Test Group", member);
        favoritePkInfo = new FavoritePkInfo(favoritePkGroup, parkingLot);

        memberId = memberRepository.save(member).getId();
        otherMemberId = memberRepository.save(otherMember).getId();
        favoriteGroupId = favoriteRepository.save(favoritePkGroup).getId();
        favoritePkInfoId = favoritePkInfoRepository.save(favoritePkInfo).getId();
        parkingLotId = parkingLotRepository.save(parkingLot).getId();
    }

    @AfterEach
    public void afterTest() {
        favoriteRepository.deleteAll();
    }

    @Nested
    @DisplayName("특정 즐겨찾기 그룹에 속한 주차장 데이터를 조회")
    class getFavoriteInfosTest {
        @Test
        @DisplayName("유효하지 않은 정렬 기준으로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByUnSupportedCondition() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.getFavoriteInfos(memberId, "InValid", favoriteGroupId, cursorId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터를 조회한 결과가 존재하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.getFavoriteInfos(memberId, sortCondition, favoriteGroupId, 0L))
                    .isInstanceOf(BaseException.class);

        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetFavoriteInfos() {
            // when
            FavoriteQueryResponse<FavoritePkInfoResponse> response = favoritePkInfoService.getFavoriteInfos(memberId, sortCondition, favoriteGroupId, cursorId);

            // then
            assertThat(response.getFavoriteList().size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("특정 즐겨찾기 그룹에 주차장 데이터 추가")
    class createFavoriteInfoTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.createFavoriteInfo(errorMemberId, favoriteGroupId, parkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 favoriteGroupId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteGroupId() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.createFavoriteInfo(memberId, errorFavoriteGroupId, parkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 parkingLotId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLotId() {
            assertThatThrownBy(() -> favoritePkInfoService.createFavoriteInfo(memberId, favoriteGroupId, errorParkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 주차장의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLot() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.createFavoriteInfo(otherMemberId, favoriteGroupId, parkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("주차장 즐겨찾기에 성공한다")
        public void successCreateFavoriteInfo() {
            // when
            favoritePkInfoService.createFavoriteInfo(memberId, favoriteGroupId, parkingLotId);

            // then
            assertThat(favoritePkInfoRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("특정 즐겨찾기 그룹에서 주차장 데이터 삭제")
    class deleteFavoriteInfoTest {
        @Test
        @DisplayName("유효하지 않은 favoriteInfoId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteInfoId() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.deleteFavoriteInfo(memberId, errorFavoriteInfoId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoritePkInfoService.deleteFavoriteInfo(errorMemberId, favoritePkInfoId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("즐겨찾기된 주차장 제거에 성공한다")
        public void successDeleteFavoriteInfo() {
            // when
            favoritePkInfoService.deleteFavoriteInfo(memberId, favoritePkInfoId);

            // then
            assertThat(favoritePkInfoRepository.findById(favoritePkInfoId)).isNotPresent();
        }
    }
}