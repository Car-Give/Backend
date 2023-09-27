package com.example.cargive.parkinglot.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.parkingLot.controller.dto.request.ParkingLotRequest;
import com.example.cargive.domain.parkingLot.controller.dto.response.ParkingLotResponse;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.service.ParkingLotService;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParkingLot [Service Layer] -> ParkingLotService 테스트")
public class ParkingLotServiceTest extends ServiceTest {
    @Autowired
    private ParkingLotService parkingLotService;
    private ParkingLot parkinglot;
    private Long parkingLotId;
    private Long errorParkingLotId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        parkinglot = PARKING_LOT_1.createEntity();
        parkingLotId = parkingLotRepository.save(parkinglot).getId();
    }

    @AfterEach
    public void afterTest() {
        parkingLotRepository.deleteAll();
    }

    @Nested
    @DisplayName("ParkingLot Entity 단일 조회")
    class findParkingLotTest {
        @Test
        @DisplayName("유효하지 않은 parkingLotId로 요청할 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLotId() {
            // when - then
            assertThatThrownBy(() -> parkingLotService.findParkingLot(errorParkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("주차장 데이터 조회에 성공한다")
        public void successFindParkingLot() {
            // when
            ParkingLotResponse responseData = parkingLotService.findParkingLot(parkingLotId);

            // then
            assertAll(
                    () -> assertThat(responseData.latitude()).isEqualTo(parkinglot.getLatitude()),
                    () -> assertThat(responseData.longitude()).isEqualTo(parkinglot.getLongitude())
            );
        }
    }

    @Nested
    @DisplayName("ParkingLot Entity 생성")
    class createParkingLotTest {
        @Test
        @DisplayName("ParkingLot Entity 생성에 성공한다")
        public void successCreateParkingLot() {
            // when
            parkingLotService.createParkingLot(getCreateRequest());

            // then
            assertThat(parkingLotRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("ParkingLot Entity 삭제")
    class deleteParkingLotTest {
        @Test
        @DisplayName("유효하지 않은 parkingLotId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidParkingLotId() {
            // when - then
            assertThatThrownBy(() -> parkingLotService.deleteParkingLot(errorParkingLotId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("주차장 정보 삭제에 성공한다")
        public void successDeleteParkingLot() {
            // when
            parkingLotService.deleteParkingLot(parkingLotId);

            // then
            assertThat(parkinglot.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private ParkingLotRequest getCreateRequest() {
        return new ParkingLotRequest(PARKING_LOT_1.getLatitude(), PARKING_LOT_1.getLongitude());
    }
}