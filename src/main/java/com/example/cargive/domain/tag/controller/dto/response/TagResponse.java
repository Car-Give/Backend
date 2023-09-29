package com.example.cargive.domain.tag.controller.dto.response;

import com.example.cargive.domain.tag.entity.Tag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String name;

    @QueryProjection
    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse toDto(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}