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

    private boolean status; // 데이터의 상태를 관리하기 위한 컬럼

    public History(Car car, ParkingLot parkingLot) {
        this.car = car;
        this.parkingLot = parkingLot;
        this.status = false;
    }

    // Car Entity와 연관 관계를 맺는 메서드
    public void initCar(Car car) {
        this.car = car;
    }

    // 데이터의 상태를 변경하는 메서드
    public void editInfo() {
        this.status = true;
    }
}