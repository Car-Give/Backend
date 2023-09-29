package com.example.cargive.parkinglot.domain;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.template.Status;
import com.example.cargive.parkinglot.fixture.ParkingLotFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParkingLot 도메인 테스트")
public class ParkingLotTest {
    private ParkingLot parkingLot;

    @BeforeEach
    public void initTest() {
        parkingLot = ParkingLotFixture.PARKING_LOT_1.createEntity();
    }

    @Test
    @DisplayName("ParkingLot 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(parkingLot.getLongitude()).isEqualTo(1.0),
                () -> assertThat(parkingLot.getLatitude()).isEqualTo(1.0),
                () -> assertThat(parkingLot.getStatus()).isEqualTo(Status.NORMAL)
        );
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        parkingLot.deleteEntity();

        assertThat(parkingLot.getStatus()).isEqualTo(Status.EXPIRED);
    }
}
