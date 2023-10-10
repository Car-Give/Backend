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

    @Column(nullable = false)
    private boolean isFavorite; // 자동차의 즐겨찾기 유무를 저장하기 위한 컬럼

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
        this.isFavorite = false;
        this.status = Status.NORMAL;
        this.member = member;
    }

    // 데이터의 상태를 변경하기 위한 메서드
    public void deleteEntity() {
        this.status = Status.EXPIRED;
    }

    // 데이터의 상태를 변경하기 위한 메서드
    public void favoriteEntity() {
        this.isFavorite = true;
    }

    // 데이터의 수정을 위한 메서드
    public void editInfo(LocalDate recentCheck, Long mileage, String imageUrl, List<Tag> addedList, List<Tag> deleteList) {
        this.recentCheck = recentCheck;
        this.mileage = mileage;
        editImageUrl(imageUrl);
        manageTagConnection(addedList, deleteList);
    }

    // Tag Entity와 연관 관계를 맺기 위한 메서드
    public void addTag(Tag tag) {
        if(tag.getCar() == null) tag.initCar(this);
        this.tagList.add(tag);
    }

    // History Entity와 연관 관계를 맺기 위한 메서드
    public void addHistory(History history) {
        if(history.getCar() == null) history.initCar(this);
        this.historyList.add(history);
    }

    // Tag Entity와의 연관 관계를 지우기 위한 메서드
    public void removeTag(Tag tag) {
        this.tagList.remove(tag);
    }

    public void initMember(Member member) {
        this.member = member;
    }

    // 이미지 접근 URL을 수정하기 위한 메서드
    private void editImageUrl(String imageUrl) {
        if(imageUrl.isBlank() || imageUrl.isEmpty()) return;
        this.imageUrl = imageUrl;
    }

    // Car Entity 수정시, 새롭게 추가되거나 삭제되는 차량 특징 카드와의 연관 관계를 관리하기 위한 메서드
    private void manageTagConnection(List<Tag> addedList, List<Tag> deletedList) {
        if(!deletedList.isEmpty()) this.tagList.removeAll(deletedList);
        if(!addedList.isEmpty()) this.tagList.addAll(addedList);
    }
}