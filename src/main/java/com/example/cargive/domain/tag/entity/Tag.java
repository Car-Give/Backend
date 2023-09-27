package com.example.cargive.domain.tag.entity;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 태그의 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car; // Car Entity와 연관 관계를 형성

    @Convert(converter = Status.StatusConverter.class)
    private Status status; // 데이터의 상태를 관리하기 위한 Enum 필드

    @Builder
    public Tag(String name) {
        this.name = name;
        this.status = Status.NORMAL;
    }

    // Car Entity와 연관 관계를 맺기 위한 메서드
    public void initCar(Car car) {
        this.car = car;
    }

    // 데이터의 상태를 변경하기 위한 메서드
    public void deleteEntity() {
        this.status = Status.EXPIRED;
    }
}