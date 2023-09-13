package com.example.cargive.domain.favorites.entity;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorites")
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "favorite_name", length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long count;

    @OneToMany(mappedBy = "favorite",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ParkingLot> parkingLots;

    @Builder
    public Favorite(String name, Long count){
        this.name = name;
        this.count = count;
        this.parkingLots = new ArrayList<>();
    }

    public void upCount(){
        this.count++;
    }
    public void downCount() { this.count--; }

}
