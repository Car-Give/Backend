package com.example.cargive.domain.history.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class HistoryResponse {
    private long id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean status;

    @QueryProjection
    public HistoryResponse(Long id, LocalDateTime createAt, LocalDateTime updateAt, boolean status) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}