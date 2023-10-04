package com.example.cargive.history.domain;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.history.entity.History;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.member.fixture.MemberFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.history.fixture.HistoryFixture.*;
import static com.example.cargive.parkinglot.fixture.ParkingLotFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("History 도메인 테스트")
public class HistoryTest {
    private History history;
    private Car car;
    private Car otherCar;
    private Member member;
    private ParkingLot parkingLot;

    @BeforeEach
    public void initTest() {
        member = MemberFixture.WIZ.toMember();
        car = CAR_1.createEntityWithMember(member);
        otherCar = CAR_2.createEntityWithMember(member);
        parkingLot = PARKING_LOT_1.createEntity();
        history = HISTORY_1.createEntity(car, parkingLot);
    }

    @Test
    @DisplayName("History 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(history.getCar().getType()).isEqualTo(car.getType()),
                () -> assertThat(history.getParkingLot().getLongitude()).isEqualTo(parkingLot.getLongitude()),
                () -> assertThat(history.getParkingLot().getLatitude()).isEqualTo(parkingLot.getLatitude()),
                () -> assertThat(history.isStatus()).isFalse()
        );
    }

    @Test
    @DisplayName("initCar() 메서드 테스트")
    public void initCarTest() {
        // when
        history.initCar(otherCar);

        // then
        assertThat(history.getCar().getType()).isEqualTo(otherCar.getType());
    }

    @Test
    @DisplayName("editInfo() 메서드 테스트")
    public void editInfoTest() {
        // when
        history.editInfo();

        // then
        assertThat(history.isStatus()).isTrue();
    }
}