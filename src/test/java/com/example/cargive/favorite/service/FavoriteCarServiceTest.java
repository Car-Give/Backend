package com.example.cargive.favorite.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.service.FavoriteCarService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("FavoriteCar [Service Layer] -> FavoriteCarService 테스트")
public class FavoriteCarServiceTest extends ServiceTest {
    @Autowired
    private FavoriteCarService favoriteCarService;
    private Car car;
    private Car otherCar;
    private Member member;
    private Member otherMember;
    private FavoritePkGroup favoritePkGroup;
    private FavoriteCar favoriteCar;
    private Long memberId;
    private Long otherMemberId;
    private Long carId;
    private Long otherCarId;
    private Long favoriteCarId;
    private Long errorFavoriteId;
    private Long errorFavoriteCarId = Long.MAX_VALUE;
    private Long errorCarId = Long.MAX_VALUE;
    private Long errorMemberId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        member = MEMBER_1.createEntity();
        otherMember = MEMBER_2.createEntity();
        car = CAR_1.createEntityWithMember(member);
        otherCar = CAR_2.createEntityWithMember(otherMember);
        favoriteCar = new FavoriteCar(member, car);
        favoritePkGroup = new FavoritePkGroup("TestTitle", member);

        carId = carRepository.save(car).getId();
        otherCarId = carRepository.save(otherCar).getId();
        memberId = memberRepository.save(member).getId();
        otherMemberId = memberRepository.save(otherMember).getId();
        favoriteCarId = favoriteRepository.save(favoriteCar).getId();
        errorFavoriteId = favoriteRepository.save(favoritePkGroup).getId();
    }

    @AfterEach
    public void afterTest() {
        favoriteRepository.deleteAll();
    }

    @Nested
    @DisplayName("차량을 즐겨찾기에 등록")
    class createFavoriteCarTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.createFavoriteCar(errorMemberId, carId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.createFavoriteCar(memberId, errorCarId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.createFavoriteCar(otherMemberId, carId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("차량 즐겨찾기에 성공한다")
        public void successCreateFavoriteCar() {
            // when
            favoriteCarService.createFavoriteCar(otherMemberId, otherCarId);

            // then
            assertThat(favoriteRepository.findAll().size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("즐겨찾기된 자동차를 제거")
    class deleteFavoriteCar {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.deleteFavoriteCar(errorMemberId, favoriteCarId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 favoriteCarId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteCarId() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.deleteFavoriteCar(memberId, errorFavoriteCarId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("조회한 데이터의 타입이 잘못된 경우, 오류를 반환한다")
        public void throwExceptionByInvalidFavoriteType() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.deleteFavoriteCar(memberId, errorFavoriteId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> favoriteCarService.deleteFavoriteCar(otherMemberId, favoriteCarId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("즐겨찾기된 자동차 정보 삭제에 성공한다")
        public void successDeleteFavoriteCar() {
            // when
            favoriteCarService.deleteFavoriteCar(memberId, favoriteCarId);

            // then
            assertThat(favoriteCar.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }
}