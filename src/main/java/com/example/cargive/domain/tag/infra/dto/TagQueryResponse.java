package com.example.cargive.domain.tag.infra.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TagQueryResponse<T> {
    private List<T> tagList = new ArrayList<>();

    public TagQueryResponse(List<T> tagList) {
        this.tagList = tagList;
    }
}