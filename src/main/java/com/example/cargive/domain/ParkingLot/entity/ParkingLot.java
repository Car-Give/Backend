package com.example.cargive.domain.ParkingLot.entity;

import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "parking_lot")
public class ParkingLot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String fee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    @Builder
    public ParkingLot(String name, String phoneNumber, String address, String fee, Favorite favorite) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fee = fee;
        this.favorite = favorite;
        favorite.getParkingLots().add(this);
    }

    public void updateFavoriteCount(){
        favorite.downCount();
    }
}