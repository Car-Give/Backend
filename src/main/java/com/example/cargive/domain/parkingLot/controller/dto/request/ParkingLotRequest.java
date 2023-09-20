package com.example.cargive.domain.parkingLot.controller.dto.request;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import jakarta.validation.constraints.NotNull;

// 주차장의 위도와 경도값을 통해 ParkingLot Entity를 생성하기 위한 DTO 파일
public record ParkingLotRequest (
        @NotNull(message = "주차장의 위도 값을 입력해주세요.")
        Double latitude, // 주차장의 위도

        @NotNull(message = "주차장의 경도 값을 입력해주세요.")
        Double longitude // 주차장의 경도
) {
        public ParkingLot toParkingLot() {
                return ParkingLot.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
        }
}