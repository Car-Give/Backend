package com.example.cargive.domain.favorite.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePkGroup extends Favorite {
    @Column
    private String name; // 즐겨찾기 그룹 이름을 저장하기 위한 컬럼

    @OneToMany(mappedBy = "favoritePkGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FavoritePkInfo> infoList = new ArrayList<>(); // FavoritePkInfo Entity와 양방향 연관 관계를 형성
}
