package com.example.cargive.domain.tag.infra;

import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.domain.tag.infra.dto.TagQueryResponse;

public interface TagQueryRepository {
    TagQueryResponse<TagResponse> findTagsByCar(Long carId);
}