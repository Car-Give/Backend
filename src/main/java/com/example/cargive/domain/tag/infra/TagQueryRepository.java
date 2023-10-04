package com.example.cargive.domain.tag.infra;

import com.example.cargive.domain.tag.controller.dto.response.TagResponse;

import java.util.List;

public interface TagQueryRepository {
    List<TagResponse> findTagsByCar(Long carId);
}