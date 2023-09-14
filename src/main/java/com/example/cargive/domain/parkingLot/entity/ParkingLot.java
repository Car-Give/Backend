package com.example.cargive.domain.parkingLot.entity;

import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "parking_lot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParkingLot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude; // 위도를 저장하기 위한 컬럼

    @Column(nullable = false)
    private Double longitude; // 경도를 저장하기 위한 컬럼

    @Convert(converter = Status.StatusConverter.class)
    private Status status; // 데이터의 상태를 관리하기 위한 Enum 필드

    @Builder
    public ParkingLot(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = Status.NORMAL;
    }
}