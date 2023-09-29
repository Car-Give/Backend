package com.example.cargive.domain.favorite.entity;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.template.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCar extends Favorite {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car; // Car Entity와 단방향 연관 관계를 형성

    public FavoriteCar(Member member, Car car) {
        this.member = member;
        this.car = car;
        this.status = Status.NORMAL;
    }
}