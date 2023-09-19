package com.example.cargive.domain.parkingLot.controller.dto.response;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;

// 데이터베이스에 저장된 주차장 정보를 노출하기 위한 DTO 파일
public record ParkingLotResponse(
        Long id, // 데이터의 PK값
        Double latitude, // 데이터의 위도
        Double longitude // 데이터의 경도
) {
    public static ParkingLotResponse toDto(ParkingLot parkingLot) {
        return new ParkingLotResponse(parkingLot.getId(), parkingLot.getLatitude(), parkingLot.getLongitude());
    }
}
