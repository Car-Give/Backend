package com.example.cargive.domain.Favorites.entity;

import com.example.cargive.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Favorites")
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "favorite_name", length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long count;

    @Builder
    public Favorite(String name, Long count){
        this.name = name;
        this.count = count;
    }

}
