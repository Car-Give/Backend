package com.example.cargive.domain.question.entity;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Convert(converter = Category.CategoryConverter.class)
    private Category category;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // Member Entity와 단방향 연관 관계를 형성

    @Builder
    public Question(String title, String content, Category category, Member member) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = Status.NORMAL;
        this.member = member;
    }
}
