package com.example.cargive.history.fixture;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.history.entity.History;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryFixture {
    HISTORY_1(1L, false),
    HISTORY_2(2L, false);

    private Long id;
    private boolean status;

    public History createEntity(Car car, ParkingLot parkingLot) {
        return new History(car, parkingLot);
    }

    public History createEntity() {
        return new History(null, null);
    }
}
