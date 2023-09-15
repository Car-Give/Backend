package com.example.cargive.domain.history.entity;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car; // Car Entity와 양방향 연관 관계를 형성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot; // ParkingLot Entity와 단방향 연관 관계를 형성
}