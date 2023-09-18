package com.example.cargive.domain.answer.entity;

import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;  // Question Entity와 단방향 연관 관계를 형성

    @Builder
    public Answer(String content, Question question) {
        this.content = content;
        this.status = Status.NORMAL;
        this.question = question;
    }
}
