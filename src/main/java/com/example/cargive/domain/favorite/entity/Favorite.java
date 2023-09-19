package com.example.cargive.domain.favorite.entity;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorColumn
@Table(name = "favorite")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    protected Long id;

    @Convert(converter = Status.StatusConverter.class)
    protected Status status; // 데이터의 상태를 관리하기 위한 Enum 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member; // Member Entity와 양방향 연관 관계를 형성

    public void deleteEntity() {
        this.status = Status.EXPIRED; // 데이터의 상태를 변경하기 위한 메서드 추가
    }
}