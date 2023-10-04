package com.example.cargive.history.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.history.controller.dto.HistoryResponse;
import com.example.cargive.domain.history.entity.History;
import com.example.cargive.domain.history.service.HistoryService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.base.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.history.fixture.HistoryFixture.*;
import static com.example.cargive.member.fixture.MemberFixture.*;
import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("History [Service Layer] -> HistoryService 테스트")
public class HistoryServiceTest extends ServiceTest {
    @Autowired
    private HistoryService historyService;

    private Member member;
    private Member otherMember;
    private ParkingLot parkingLot;
    private Car car;
    private Car otherCar;
    private History history;
    private Long memberId;
    private Long otherMemberId;
    private Long parkingLotId;
    private Long carId;
    private Long otherCarId;
    private Long historyId;
    private Long errorMemberId = Long.MAX_VALUE;
    private Long errorParkingLotId = Long.MAX_VALUE;
    private Long errorCarId = Long.MAX_VALUE;
    private Long errorHistoryId = Long.MAX_VALUE;


    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        otherMember = ASSAC.toMember();
        parkingLot = PARKING_LOT_1.createEntity();
        car = CAR_1.createEntityWithMember(member);
        otherCar = CAR_2.createEntityWithMember(otherMember);
        history = HISTORY_1.createEntity(car, parkingLot);

        memberId = memberRepository.save(member).getId();
        otherMemberId = memberRepository.save(otherMember).getId();
        parkingLotId = parkingLotRepository.save(parkingLot).getId();
        carId = carRepository.save(car).getId();
        otherCarId = carRepository.save(otherCar).getId();
        historyId = historyRepository.save(history).getId();
    }

    @AfterEach
    public void afterTest() {
        historyRepository.deleteAll();
    }

    @Nested
    @DisplayName("특정 차량의 이용 기록 조회")
    class getHistoryListTest {
        @Test
        @DisplayName("데이터를 조회한 결과가 존재하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when - then
            assertThatThrownBy(() -> historyService.getHistoryList(errorCarId, errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetHistoryList() {
            // when
            List<HistoryResponse> responseList = historyService.getHistoryList(carId, memberId);

            // then
            assertThat(responseList.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("이용 시작")
    class startUseHistoryTest {
        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> historyService.startUseHistory(carId, parkingLotId, errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 parkingLotId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLotId() {
            // when - then
            assertThatThrownBy(() -> historyService.startUseHistory(carId, errorParkingLotId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> historyService.startUseHistory(errorCarId, parkingLotId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 이용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> historyService.startUseHistory(carId, parkingLotId, otherMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("이용 기록을 시작한다")
        public void successStartUsingHistory() {
            // when
            historyService.startUseHistory(carId, parkingLotId, memberId);

            // then
            assertThat(historyRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("이용 종료")
    class endUseHistoryTest {
        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> historyService.endUseHistory(historyId, errorCarId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 memberId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMemberId() {
            // when - then
            assertThatThrownBy(() -> historyService.endUseHistory(historyId, carId, errorMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 historyId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidHistoryId() {
            // when - then
            assertThatThrownBy(() -> historyService.endUseHistory(errorHistoryId, carId, memberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 이용자의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidMember() {
            // when - then
            assertThatThrownBy(() -> historyService.endUseHistory(historyId, carId, otherMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("저장된 데이터와 차량의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCar() {
            // when - then
            assertThatThrownBy(() -> historyService.endUseHistory(historyId, otherCarId, otherMemberId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("이용을 종료한다")
        public void successEndUseHistory() {
            // when
            historyService.endUseHistory(historyId, carId, memberId);

            // then
            assertThat(history.isStatus()).isTrue();
        }
    }
}