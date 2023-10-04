package com.example.cargive.car.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.car.controller.dto.request.CarEditRequest;
import com.example.cargive.domain.car.controller.dto.request.CarRequest;
import com.example.cargive.domain.car.controller.dto.response.CarResponse;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.service.CarService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.member.fixture.MemberFixture.*;
import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Car [Service Layer] -> CarService 테스트")
public class CarServiceTest extends ServiceTest {
    @Autowired
    private CarService carService;

    private Car car;
    private Member member;
    private Member otherMember;
    private Tag tag;
    private Long carId;
    private Long memberId;
    private Long otherMemberId;
    private Long errorCarId = Long.MAX_VALUE;
    private Long errorMemberId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        memberId = memberRepository.save(member).getId();

        otherMember = ASSAC.toMember();
        otherMemberId = memberRepository.save(otherMember).getId();

        car = CAR_1.createEntityWithMember(member);
        carId = carRepository.save(car).getId();

        tag = TAG_1.createEntity();
        tagRepository.save(tag);

        tag.initCar(car);
        car.addTag(tag);
    }

    @AfterEach
    public void afterTest() {
        carRepository.deleteAll();
    }

    @Nested
    @DisplayName("데이터 조회")
    class getCarListTest {
        @Test
        @DisplayName("조회한 데이터가 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when - then
            assertThatThrownBy(() -> carService.getCarList(errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetCarList() {
            // when
            List<CarResponse> responseList = carService.getCarList(memberId);

            // then
            assertThat(responseList.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("데이터 생성")
    class createCarTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> carService.createCar(getCarRequest(),
                    new MockMultipartFile("Test Image", "test",  null, new byte[]{}),
                    errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("자동차 정보 생성에 성공한다")
        public void successCreateCar() {
            // when
            carService.createCar(
                    getCarRequest(),
                    new MockMultipartFile("Test Image", "test",  null, new byte[]{}),
                    memberId);

            // then
            assertThat(carRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("데이터 수정")
    class editCarTest {
        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> carService.editCar(getCarEditRequest(),
                    new MockMultipartFile("Test Image", "test",  null, new byte[]{}),
                    errorCarId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> carService.editCar(getCarEditRequest(),
                    new MockMultipartFile("Test Image", "test",  null, new byte[]{}),
                    carId, errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("차량 정보 수정에 성공한다")
        public void successEditCar() {
            // when
            carService.editCar(getCarEditRequest(),
                    new MockMultipartFile("Test Image", "test", null, new byte[]{}),
                    carId, memberId);

            // then
            Assertions.assertAll(
                    () -> assertThat(car.getMileage()).isEqualTo(10L),
                    () -> assertThat(car.getRecentCheck()).isBeforeOrEqualTo(LocalDate.now())
            );
        }
    }

    @Nested
    @DisplayName("데이터 삭제")
    class deleteCarTest {
        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> carService.deleteCar(errorCarId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> carService.deleteCar(carId, errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> carService.deleteCar(carId, otherMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 삭제에 성공한다")
        public void successDeleteCar() {
            // when
            carService.deleteCar(carId, memberId);

            // then
            assertThat(car.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private CarRequest getCarRequest() {
        CarRequest request =  new CarRequest();
        request.setType("Test Car Name");
        request.setMileage(100L);
        request.setRecentCheck(LocalDate.now());
        request.setTagList(Arrays.asList("Tag1", "Tag2", "Tag3"));
        request.setNumber("Test");

        return request;
    }

    private CarEditRequest getCarEditRequest() {
        CarEditRequest carEditRequest = new CarEditRequest();
        carEditRequest.setDeletedTagList(new ArrayList<>());
        carEditRequest.setDeletedTagList(new ArrayList<>());
        carEditRequest.setMileage(10L);
        carEditRequest.setRecentCheck(LocalDate.now());

        return carEditRequest;
    }
}