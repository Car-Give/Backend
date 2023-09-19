package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;

public interface FavoriteGroupQueryRepository {
    // 최신순으로 즐겨찾기 그룹을 조회
    FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroupByTime(Long memberId, int page);
    // 제목순으로 즐겨찾기 그룹을 조회
    FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroupByTitle(Long memberId, int page);
}
