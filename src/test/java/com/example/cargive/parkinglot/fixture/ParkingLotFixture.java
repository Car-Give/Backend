package com.example.cargive.parkinglot.fixture;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.template.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParkingLotFixture {
    PARKING_LOT_1(1.0, 1.0, Status.NORMAL),
    PARKING_LOT_2(2.0, 2.0, Status.NORMAL),
    PARKING_LOT_3(3.0, 3.0, Status.NORMAL),
    PARKING_LOT_4(4.0, 4.0, Status.NORMAL),
    PARKING_LOT_5(5.0, 5.0, Status.NORMAL)
    ;

    private final Double longitude;
    private final Double latitude;
    private final Status status;

    public ParkingLot createEntity() {
        return new ParkingLot(latitude, longitude);
    }
}