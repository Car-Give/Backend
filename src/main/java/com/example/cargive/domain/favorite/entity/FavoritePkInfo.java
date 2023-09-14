package com.example.cargive.domain.favorite.entity;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "favorite_pk_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePkInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_pk_group")
    private FavoritePkGroup favoritePkGroup; // FavoritePkGroup Entity와 양방향 연관 관계를 형성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot; // ParkingLot Entity와 단방향 연관 관계를 형성
}
