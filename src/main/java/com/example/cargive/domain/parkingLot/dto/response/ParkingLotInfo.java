package com.example.cargive.domain.parkingLot.dto.response;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingLotInfo {

    private Long parkingLotId;
    private String name;
    private String phoneNumber;
    private String address;
    private String fee;

}
