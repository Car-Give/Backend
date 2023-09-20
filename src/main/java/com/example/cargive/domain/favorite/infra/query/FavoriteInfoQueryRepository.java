package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteInfoSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;

public interface FavoriteInfoQueryRepository {
    // 즐겨찾기 된 주차장을 조회(최신순)
    FavoriteQueryResponse<FavoritePkInfoResponse> getMyFavoritePkInfo(FavoriteInfoSortCondition sortCondition,
                                                                      Long memberId, Long favoriteGroupId,
                                                                      Long cursorId);
}