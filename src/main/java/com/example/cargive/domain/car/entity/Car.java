package com.example.cargive.domain.car.entity;

import com.example.cargive.domain.history.entity.History;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "car")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String type; // 차종을 저장하기 위한 컬럼

    @Column(nullable = false, length = 20)
    private String number; // 차량 번호를 저장하기 위한 컬럼

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recentCheck; // 최근 차량 점검일을 저장하기 위한 컬럼

    @Column(nullable = false, columnDefinition = "BIGINT default 0")
    private Long mileage; // 주행 거리를 저장하기 위한 컬럼

    @Column(nullable = false)
    private String imageUrl; // 자동차 이미지 URL을 저장하기 위한 컬럼

    @Convert(converter = Status.StatusConverter.class)
    private Status status; // 데이터의 상태를 관리하기 위한 Enum 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // Member Entity와 양방향 연관 관계를 형성

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Tag> tagList = new ArrayList<>(); // Tag Entity와 양방향 연관 관계를 형성

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<History> historyList = new ArrayList<>(); // History Entity와 양방향 연관 관계를 형성

    @Builder
    public Car(String type, String number, LocalDate recentCheck, Long mileage, String imageUrl, Member member) {
        this.type = type;
        this.number = number;
        this.recentCheck = recentCheck;
        this.mileage = mileage;
        this.imageUrl = imageUrl;
        this.status = Status.NORMAL;
        this.member = member;
    }
}