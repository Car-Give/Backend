package com.example.cargive.domain.notice.entity;

import com.example.cargive.global.domain.BaseEntity;
import com.example.cargive.global.template.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
        this.status = Status.NORMAL;
    }

    public void editInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void deleteEntity() {
        this.status = Status.EXPIRED;
    }
}