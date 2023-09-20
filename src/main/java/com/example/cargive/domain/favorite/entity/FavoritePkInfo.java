package com.example.cargive.domain.favorite.entity;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "favorite_pk_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePkInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_pk_group")
    private FavoritePkGroup favoritePkGroup; // FavoritePkGroup Entity와 양방향 연관 관계를 형성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot; // ParkingLot Entity와 단방향 연관 관계를 형성

    @Builder
    public FavoritePkInfo(FavoritePkGroup favoritePkGroup, ParkingLot parkingLot) {
        this.favoritePkGroup = favoritePkGroup;
        this.parkingLot = parkingLot;
    }

    // 연관 관계 메서드 추가
    public void initFavoritePkGroup(FavoritePkGroup favoritePkGroup) {
        if(this.favoritePkGroup == null) this.favoritePkGroup = favoritePkGroup;
    }
}
