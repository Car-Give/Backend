package com.example.cargive.domain.favorite.controller.dto.response;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 즐겨찾기된 주차장 정보를 조회하는 경우, 직접적으로 Entity가 노출되는 것을 피하기 위해 별도의 DTO 파일 생성
@Getter
@NoArgsConstructor
public class FavoritePkInfoResponse {
    private Long id;
    private Double latitude; // 즐겨찾기된 주차장의 위도
    private Double longitude; // 즐겨찾기된 주차장의 경도
    private LocalDateTime createAt; // 즐겨찾기된 데이터를 정렬하기 위하여 선언된 필드

    @QueryProjection // querydsl을 위한 어노테이션
    public FavoritePkInfoResponse(Long id, ParkingLot parkingLot, LocalDateTime createAt) {
        this.id = id;
        this.latitude = parkingLot.getLatitude();
        this.longitude = parkingLot.getLongitude();
        this.createAt = createAt;
    }
}
