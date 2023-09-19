package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;

public interface FavoriteInfoQueryRepository {
    // 최신순으로 즐겨찾기 된 주차장을 조회
    FavoriteQueryResponse<FavoritePkInfoResponse> getMyFavoritePkInfo(Long memberId, Long favoriteGroupId, int page);
}
