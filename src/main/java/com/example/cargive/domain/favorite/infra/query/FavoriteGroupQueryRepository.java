package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteGroupSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;

public interface FavoriteGroupQueryRepository {
    // 즐겨찾기 그룹을 조회(최신순, 제목순)
    FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroup(FavoriteGroupSortCondition sortCondition,
                                                                              Long memberId, Long cursorId);
}
