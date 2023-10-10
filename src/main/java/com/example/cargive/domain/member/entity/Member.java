package com.example.cargive.domain.member.entity;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.global.template.Status;
import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String name;

    private String phoneNumber;

    @Embedded
    private Email email;

    @Convert(converter = Social.SocialConverter.class)
    private Social social;

    private String imageUrl;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private List<Car> carList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private List<FavoritePkGroup> favoritePkGroupList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private List<FavoriteCar> favoriteCarList = new ArrayList<>();

    @Builder
    public Member(String loginId, String name, String phoneNumber, Email email, Social social, String imageUrl) {
        this.loginId = loginId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.social = social;
        this.imageUrl = imageUrl;
        this.status = Status.NORMAL;
    }

    public void addFavoritePkGroup(FavoritePkGroup favoritePkGroup) {
        if(favoritePkGroup.getMember() == null) favoritePkGroup.initMember(this);
        this.favoritePkGroupList.add(favoritePkGroup);
    }

    public void addFavoriteCar(FavoriteCar favoriteCar) {
        if(favoriteCar.getMember() == null) favoriteCar.initMember(this);
        this.favoriteCarList.add(favoriteCar);
    }

    public void addCar(Car car) {
        if(car.getMember() == null) car.initMember(this);
        this.carList.add(car);
    }

    public void removeCar(Car car) {
        this.carList.remove(car);
    }

    public void removeFavoritePkGroup(FavoritePkGroup favoritePkGroup) {
        this.favoritePkGroupList.remove(favoritePkGroup);
    }

    public void removeFavoriteCar(FavoriteCar favoriteCar) {
        this.favoriteCarList.remove(favoriteCar);
    }

    public void deleteEntity() {
        this.status = Status.EXPIRED;
    }

    public void editInfo(String phoneNumber, String imageUrl) {
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }
}
