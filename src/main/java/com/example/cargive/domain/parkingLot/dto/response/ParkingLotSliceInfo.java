package com.example.cargive.domain.ParkingLot.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingLotSliceInfo {
    private List<ParkingLotInfo> parkingLotInfoList;
}
